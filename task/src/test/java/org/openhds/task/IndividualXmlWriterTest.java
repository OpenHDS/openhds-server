package org.openhds.task;

import org.junit.Test;

public class IndividualXmlWriterTest {

    @Test
    public void shouldWriteIndividualXml() {
        IndividualXmlWriter individualXmlWriter = new IndividualXmlWriter();
        
        individualXmlWriter.writeIndividualsToXmlFile("individuals.xml");
        
        
    }

}
