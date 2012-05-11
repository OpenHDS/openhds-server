package org.openhds.configuration.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ValueConstraintService {
	
	private Document doc;
	
	public ValueConstraintService() {
		SAXBuilder builder = new SAXBuilder();
		try {	
			File currentDirectory = new File("");
			String path = currentDirectory.getAbsolutePath();
			
			path = path.replace("dataextensions", "");
			
			InputStream is = new FileInputStream("../domain/src/main/resources/value-constraint.xml");
			doc = builder.build(is);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public boolean isConstraintDefined(String constraintName) {
		Element ele = findConstraintByName(constraintName);
		return ele != null ? true : false;
	}
	
	private Element findConstraintByName(String constraintName) {
		List list = doc.getRootElement().getChildren();
		for (int i = 0; i < list.size(); i++) {
			Element ele = (Element)list.get(i);
			if (ele.getAttribute("id").getValue().equals(constraintName)) 
				return ele;
		}
		return null;
	}
	
	public boolean isValidConstraintValue(String constraintName, Object value) {
		List list = doc.getRootElement().getChildren();
		for (int i = 0; i < list.size(); i++) {
			Element ele = (Element)list.get(i);
			if (ele.getAttribute("id").getValue().equals(constraintName)) {
				List children = ele.getChildren();
				for (int j = 0; j < children.size(); j++) {
					Element ele2 = (Element)children.get(j);
					if (ele2.getValue().equals(value))
						return true;
				}
				return false;
			}
		}
		return false;
	}
	
	public List<String> getAllConstraintNames() {
		
		List<String> output = new ArrayList<String>();
		List list = doc.getRootElement().getChildren();
		for (int i = 0; i < list.size(); i++) {
			Element ele = (Element)list.get(i);
			output.add(ele.getAttribute("id").getValue()); 
		}
		return output;
	}
	
	public Map<String, String> getMapForConstraint(String constraintName) {
		Map<String, String> keyValues = new TreeMap<String, String>();
		Element constraint = findConstraintByName(constraintName);
		List children = constraint.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Element child = (Element)children.get(i);
			keyValues.put(child.getValue(), child.getAttributeValue("description"));
		}
		return keyValues;
	}
}