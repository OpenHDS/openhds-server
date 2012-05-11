package org.openhds.configuration.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ExtensionService {
	
	private Document doc;
	
	public ExtensionService() {
		
		try {
		
			File currentDirectory = new File("");
			String path = currentDirectory.getAbsolutePath();
			
			path = path.replace("dataextensions", "");
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
	
			File configFile = new File(path + "/domain/src/main/resources/extension-config.xml");
			doc = builder.parse(configFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, ArrayList<Map<String, String>>> getMapForExtensions() {
		
		HashMap<String, ArrayList<Map<String, String>>> attrMap = new HashMap<String, ArrayList<Map<String, String>>>();
		
		NodeList items = doc.getElementsByTagName("entity");
		
		for (int i = 0; i < items.getLength(); i++) {
			
			Element entityElement = (Element)items.item(i);			
			String entity = entityElement.getAttributes().getNamedItem("name").getNodeValue();
			
			// we must now traverse all attributes specific for the entity
			NodeList attributesList = entityElement.getElementsByTagName("attributes");	
			
			if (attributesList.getLength() == 0)
				continue;
		
			Element attributeElement = (Element)attributesList.item(0);
			NodeList attributes = attributeElement.getElementsByTagName("attribute");
						
			ArrayList<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
			// walk through all attributes and grab the name and type
			for (int j = 0; j < attributes.getLength(); j++) {
	
				Map<String, String> map = new HashMap<String, String>();
				
				Element element = (Element)attributes.item(j);
				String name =  element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
				String type = element.getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue();
				String description = element.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
				String constraint = element.getElementsByTagName("constraint").item(0).getChildNodes().item(0).getNodeValue();
				
				map.put("name", name);
				map.put("type", type);
				map.put("description", description);
				map.put("constraint", constraint);
				
				listMap.add(map);				
			}
			attrMap.put(entity, listMap);
		}
		return attrMap;
	}

}
