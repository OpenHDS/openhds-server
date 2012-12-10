package org.openhds.task;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class AbstractXmlWriterTest {

    protected void compareXmlDocuments(File expectedFile, File writtenFile) throws DocumentException, IOException {
        // some ugliness to compare XML is output correctly
        // TODO: a better way of doing this?
        SAXReader reader = new SAXReader();
        Document expectedDocument = reader.read(expectedFile);
        Document outputDocument = reader.read(writtenFile);
    
        StringWriter sw1 = new StringWriter();
        XMLWriter xw1 = new XMLWriter(sw1, OutputFormat.createCompactFormat());
        xw1.write(expectedDocument);
    
        StringWriter sw2 = new StringWriter();
        XMLWriter xw2 = new XMLWriter(sw2, OutputFormat.createCompactFormat());
        xw2.write(outputDocument);
    
        assertEquals(sw1.toString(), sw2.toString());
    }

}
