package org.openhds.task.support;

public interface TaskExecutor {

    void executeIndividualXmlWriterTask();
    
    void executeFormXmlWriterTask();
    
    void executeLocationXmlWriterTask();
    
    void executeRelationshipXmlWriterTask();
    
    void executeSocialGroupXmlWriterTask();
    
    void executeVisitWriterTask(int roundNumber);

}
