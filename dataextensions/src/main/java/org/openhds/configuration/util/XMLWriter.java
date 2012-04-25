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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLWriter {
	
	private Document constraintDoc;
	private Document extensionDoc;
	
	public XMLWriter() {
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder constraintDocBuilder = docFactory.newDocumentBuilder();
			constraintDoc = constraintDocBuilder.parse("../domain/src/main/resources/value-constraint.xml");
			
			DocumentBuilder extensionDocBuilder = docFactory.newDocumentBuilder();
			extensionDoc = extensionDocBuilder.parse("../domain/src/main/resources/extension-config.xml");
		} 
		catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public void createConstraint(String id, Map<String, String> map) {
		
		Node root = constraintDoc.getFirstChild();
		
		Element element = constraintDoc.createElement("constraint");
		Attr attr = constraintDoc.createAttribute("id");
		attr.setValue(id);
		element.setAttributeNode(attr);
		root.appendChild(element);
		
		List<String> keys = new ArrayList<String>();
		keys.addAll(map.keySet());
		
		for (String key : keys) {
			
			Element item = constraintDoc.createElement("value");
			Attr itemAttr = constraintDoc.createAttribute("description");
			itemAttr.setValue(key);
			item.setAttributeNode(itemAttr);
			item.appendChild(constraintDoc.createTextNode(map.get(key)));
			element.appendChild(item);
		}
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(constraintDoc);
			StreamResult result = new StreamResult(new File("../domain/src/main/resources/value-constraint.xml"));
			
			transformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeConstraint(String id) {
		
		NodeList nodeList = constraintDoc.getElementsByTagName("constraint");
		Node nodeToRemove = null;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);

			NamedNodeMap attrs = item.getAttributes();
			Node nodeId = attrs.getNamedItem("id");
			
			if (nodeId.getNodeValue().equals(id))
				nodeToRemove = item;
		}
		constraintDoc.getFirstChild().removeChild(nodeToRemove);
			
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(constraintDoc);
			StreamResult result = new StreamResult(new File("../domain/src/main/resources/value-constraint.xml"));
			
			transformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void createExtension(String entityName, String name, String description, String type, String constraint) {
				
		NodeList items = extensionDoc.getElementsByTagName("entity");
		
		for (int i = 0; i < items.getLength(); i++) {
			
			Element entityElement = (Element)items.item(i);			
			String entity = entityElement.getAttributes().getNamedItem("name").getNodeValue();
			
			if (entity.equals(entityName)) {
				
				NodeList attributesList = entityElement.getElementsByTagName("attributes");			
				Node attrNode = attributesList.item(0);
				
				Element element = extensionDoc.createElement("attribute");
				attrNode.appendChild(element);	
				
				Element nameElement = extensionDoc.createElement("name");
				nameElement.appendChild(extensionDoc.createTextNode(name));
				Element descriptionElement = extensionDoc.createElement("description");
				descriptionElement.appendChild(extensionDoc.createTextNode(description));
				Element typeElement = extensionDoc.createElement("type");
				typeElement.appendChild(extensionDoc.createTextNode(type));
				
				if (constraint.length() > 0) {
					Element constraintElement = extensionDoc.createElement("constraint");
					constraintElement.appendChild(extensionDoc.createTextNode(constraint));
					element.appendChild(constraintElement);
				}
				else {
					Element constraintElement = extensionDoc.createElement("constraint");
					constraintElement.appendChild(extensionDoc.createTextNode("none"));
					element.appendChild(constraintElement);
				}
				
				element.appendChild(nameElement);
				element.appendChild(descriptionElement);
				element.appendChild(typeElement);
			}		
		}
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(extensionDoc);
			StreamResult result = new StreamResult(new File("../domain/src/main/resources/extension-config.xml"));
			
			transformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void removeExtension(String entity, String attribute) {
		
		NodeList items = extensionDoc.getElementsByTagName("entity");
		Node nodeToRemove = null;
		Node parent = null;
		
		for (int i = 0; i < items.getLength(); i++) {
			
			Element entityElement = (Element)items.item(i);			
			String entityName = entityElement.getAttributes().getNamedItem("name").getNodeValue();
			
			if (entity.equals(entityName)) {
				
				NodeList attributesList = entityElement.getElementsByTagName("attributes");	

				Element attributeElement = (Element)attributesList.item(0);
				NodeList attributes = attributeElement.getElementsByTagName("attribute");
				
				for (int j = 0; j < attributes.getLength(); j++) {
					
					Node item = attributes.item(j);
					Element element = (Element)attributes.item(j);
					String name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
					
					if (name.equals(attribute)) {
						nodeToRemove = item;
						parent = item.getParentNode();
					}
				}	
			}
		}
		
		parent.removeChild(nodeToRemove);
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(extensionDoc);
			StreamResult result = new StreamResult(new File("../domain/src/main/resources/extension-config.xml"));
			
			transformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
