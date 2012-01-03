package org.openhds.configuration.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLWriter {
	
	private Document doc;
	
	public XMLWriter() {
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse("../domain/src/main/resources/value-constraint.xml");
		} 
		catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public void createConstraint(String id, Map<String, String> map) {
		
		Node root = doc.getFirstChild();
		
		Element element = doc.createElement("constraint");
		Attr attr = doc.createAttribute("id");
		attr.setValue(id);
		element.setAttributeNode(attr);
		root.appendChild(element);
		
		List<String> keys = new ArrayList<String>();
		keys.addAll(map.keySet());
		
		for (String key : keys) {
			
			Element item = doc.createElement("value");
			Attr itemAttr = doc.createAttribute("description");
			itemAttr.setValue(key);
			item.setAttributeNode(itemAttr);
			item.appendChild(doc.createTextNode(map.get(key)));
			element.appendChild(item);
		}
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("../domain/src/main/resources/value-constraint.xml"));
			
			transformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
