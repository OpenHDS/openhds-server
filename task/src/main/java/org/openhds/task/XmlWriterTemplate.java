package org.openhds.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.codec.digest.DigestUtils;
import org.openhds.domain.util.CalendarAdapter;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.task.service.AsyncTaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Template for writing entities to an XML file
 * 
 * @param <T>
 *            The type of entities to write to the file
 */
public abstract class XmlWriterTemplate<T> implements XmlWriterTask {
    private static final int PAGE_SIZE = 100;

    private CalendarUtil calendarUtil;
    private AsyncTaskService asyncTaskService;
    private String taskName;

    public XmlWriterTemplate(AsyncTaskService asyncTaskService, CalendarUtil calendarUtil, String taskName) {
        this.calendarUtil = calendarUtil;
        this.asyncTaskService = asyncTaskService;
        this.taskName = taskName;
    }

    @Async
    public void writeXml(TaskContext taskContext) {
        // service methods require authorization, which in turn depend on a user
        // this will use the security context of the user who starts the task
        SecurityContextHolder.setContext(taskContext.getSecurityContext());
        asyncTaskService.beginNewTaskSession();

        try {
            OutputStream outputStream = new FileOutputStream(taskContext.getDestinationFile());

            asyncTaskService.startTask(taskName);

            long itemsWritten = 0L;
            int totalCount = getTotalEntityCount(taskContext);
            int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

            XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream);
            xmlStreamWriter.writeStartDocument();
            xmlStreamWriter.writeStartElement(getStartElementName());
            JAXBContext context = JAXBContext.newInstance(getBoundClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setAdapter(new CalendarAdapter(calendarUtil));

            for (int i = 0; i < totalPages; i++) {
                List<T> entities = getEntitiesInRange(taskContext, (i * PAGE_SIZE), PAGE_SIZE);
                for (T original : entities) {
                    T copy = makeCopyOf(original);
                    marshaller.marshal(copy, xmlStreamWriter);
                    itemsWritten += 1;
                }
                // clear the session so hibernate empties the cache
                // this is necessary so that it doesn't consume lots and lots of
                // memory
                // as the size of the database grows
                // TODO: Is there a better way of handling this? Stateless
                // Sessions?
                asyncTaskService.clearSession();
                asyncTaskService.updateTaskProgress(taskName, itemsWritten);
            }

            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.close();
            outputStream.close();

            InputStream inputStream = new FileInputStream(taskContext.getDestinationFile());
            String md5 = DigestUtils.md5Hex(inputStream);
            inputStream.close();

            asyncTaskService.finishTask(taskName, itemsWritten, md5);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            asyncTaskService.closeTaskSession();
        }
    }

    protected abstract T makeCopyOf(T original);

    protected abstract List<T> getEntitiesInRange(TaskContext taskContext, int i, int pageSize);

    protected abstract Class<?> getBoundClass();

    protected abstract String getStartElementName();

    protected abstract int getTotalEntityCount(TaskContext taskContext);

}
