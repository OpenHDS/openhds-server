package org.openhds.task.support;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class ServletFileResolver implements FileResolver, ServletContextAware {

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public File resolveIndividualXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File individualXmlFile = new File(generatedXmlFileDir, "individual.xml");

        return individualXmlFile;
    }

    protected File getGeneratedXmlFolder() {
        String fullPath = servletContext.getRealPath("/");
        File generatedXmlFileDir = new File(fullPath + File.separator + "generated-xml");
        generatedXmlFileDir.mkdirs();
        return generatedXmlFileDir;
    }

    @Override
    public File resolveLocationXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File locationXmlFile = new File(generatedXmlFileDir, "location.xml");

        return locationXmlFile;
    }

    @Override
    public File resolveRelationshipXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File relationshipXmlFile = new File(generatedXmlFileDir, "relationship.xml");

        return relationshipXmlFile;
    }

    @Override
    public File resolvesocialGroupXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File socialGroupXmlFile = new File(generatedXmlFileDir, "socialgroup.xml");

        return socialGroupXmlFile;
    }

    @Override
    public File resolveVisitXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File visitXmlFile = new File(generatedXmlFileDir, "visit.xml");

        return visitXmlFile;
    }

}
