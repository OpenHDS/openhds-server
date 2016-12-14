package org.openhds.task.support;

import java.io.File;

public interface FileResolver {

    File resolveIndividualXmlFile();

    File resolveLocationXmlFile();

    File resolveRelationshipXmlFile();

    File resolvesocialGroupXmlFile();

    File resolveVisitXmlFile();
    
    File resolveFormXmlFile();
    
    File resolveIndividualZipFile();
    
    File resolveLocationZipFile();
 
    File resolveRelationshipZipFile();
 
    File resolvesocialGroupZipFile();
 
    File resolveVisitZipFile();
 
    File resolveFormZipFile();

}
