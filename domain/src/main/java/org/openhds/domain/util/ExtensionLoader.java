package org.openhds.domain.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * This class is automatically called by maven during the compile phase. The idea behind this is to load 
 * a configuration file which contains all of the physical data extensions to be made on the core 
 * entities. Javassist is used to create those extensions by modifying the Java bytecode. More info
 * on Javassist can be found here: http://www.csg.is.titech.ac.jp/~chiba/javassist/.
 * 
 * @author Brian
 */

public class ExtensionLoader extends AppContextAware {
	
	static BufferedReader input;
	static final String STRING_TYPE = "String";
	static final String BOOLEAN_TYPE = "Boolean";
	static final String INTEGER_TYPE = "Integer";
	static final String NONE_TYPE = "none";

	/**
	 * Loads the configuration file and proceeds to walk the document.
	 * 
	 * @param args
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, URISyntaxException, ParserConfigurationException, SAXException {
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
				
			// load the configuration file
			File configFile = new File(System.getProperty("user.dir").concat("/src/main/resources/extension-config.xml"));
			Document doc = builder.parse(configFile);
			
			processDocument(doc);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method is responsible for walking the xml configuration file. It determines which entity is 
	 * specified and loads the corresponding target .class file used by javassist.
	 * 
	 * @param doc - the document tree, iterating over all entities
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RuntimeException
	 * @throws CannotCompileException
	 * @throws NotFoundException 
	 */
	public static void processDocument(Document doc) throws URISyntaxException, IOException, RuntimeException, CannotCompileException, NotFoundException {

		NodeList items = doc.getElementsByTagName("entity");
		File file = null;
		
		for (int i = 0; i < items.getLength(); i++) {
			
			// which entity is this?
			Element entityElement = (Element)items.item(i);			
			String name = entityElement.getAttributes().getNamedItem("name").getNodeValue();

			try {
				file = new File(new URI(Class.forName("org.openhds.domain.model.Location").getResource(name + ".class").toString()));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
			InputStream stream = new FileInputStream(file);
			
			// we must now traverse all attributes specific for the entity
			NodeList attributesList = entityElement.getElementsByTagName("attributes");	
			
			if (attributesList.getLength() == 0)
				continue;
		
			processAttributes(attributesList, stream);
		}
	}
	
	/**
	 * This method is responsible for obtaining all attributes specific for the entity.
	 * It puts all of the attributes in a map and then passes them on to be modified by
	 * javassist.
	 * 
	 * @param attributesList - the document tree for attributes specific for the entity
	 * @param stream - the stream contains the location of the target entity .class file
	 * @throws IOException
	 * @throws RuntimeException
	 * @throws CannotCompileException
	 * @throws NotFoundException 
	 */
	public static void processAttributes(NodeList attributesList, InputStream stream) throws IOException, RuntimeException, CannotCompileException, NotFoundException {
		
		Map<String, Map<String, String>> listAttributes = new HashMap<String, Map<String, String>>();
				
		Element attributeElement = (Element)attributesList.item(0);
		NodeList attributes = attributeElement.getElementsByTagName("attribute");
		
		// walk through all attributes and grab the name and type
		for (int i = 0; i < attributes.getLength(); i++) {
			Map<String, String> attrMap = new HashMap<String, String>();		
			
			Element element = (Element)attributes.item(i);
			String name =  element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
			String type = element.getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue();
			String description = element.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
			
			// constraints are optional attributes
			String constraint = NONE_TYPE;
			if (type.equals(STRING_TYPE))
				constraint = element.getElementsByTagName("constraint").item(0).getChildNodes().item(0).getNodeValue();
						
			attrMap.put("type", type);
			attrMap.put("description", description);
			
			if (type.equals(STRING_TYPE) && !constraint.equals(NONE_TYPE)) 
				attrMap.put("constraint", constraint);
			
			listAttributes.put(name, attrMap);		
		}
		// pass this on to be handled by javassist
		modifyByteCode(stream, listAttributes);
	}
	
	/**
	 * This method makes use of the javassist api and modifies the .class. The fields are
	 * added and their corresponding getters/setters.
	 * 
	 * @param stream - the stream contains the location of the target entity .class file
	 * @param map - a map of all attributes pertaining to an entity
	 * @throws IOException
	 * @throws RuntimeException
	 * @throws CannotCompileException
	 * @throws NotFoundException 
	 */
	public static void modifyByteCode(InputStream stream, Map<String, Map<String, String>> map) throws IOException, RuntimeException, CannotCompileException, NotFoundException {
				
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass(stream);
		ClassFile classFile = cc.getClassFile();
		classFile.setVersionToJava5();
		
		Set<String> keys = map.keySet();
		
		// look through all the attributes
		Iterator<String> keysItr = keys.iterator();
		while (keysItr.hasNext()) {
			
			// description annotation - used for DDI
			AnnotationsAttribute descAttribute = new AnnotationsAttribute(classFile.getConstPool(), AnnotationsAttribute.visibleTag);
			Annotation descAnnotation = new Annotation("org.openhds.annotations.Description", classFile.getConstPool());
			StringMemberValue stringDescValue = new StringMemberValue(classFile.getConstPool());
			
			// extension annotation - used for multiple choice answers
			AnnotationsAttribute extAttribute = new AnnotationsAttribute(classFile.getConstPool(), AnnotationsAttribute.visibleTag);
			Annotation extAnnotation = new Annotation("org.openhds.constraint.ExtensionConstraint", classFile.getConstPool());
			StringMemberValue stringExtValue = new StringMemberValue(classFile.getConstPool());
			StringMemberValue stringExtMsgValue = new StringMemberValue(classFile.getConstPool());
			
			String name = keysItr.next();
			String type = map.get(name).get("type");
			String desc = map.get(name).get("description");
			
			String constraint = null;
			if (map.get(name).containsKey("constraint")) {
				constraint = map.get(name).get("constraint");
				stringExtValue.setValue(constraint);
				stringExtMsgValue.setValue("Invalid Value for " + name);
				extAnnotation.addMemberValue("constraint", stringExtValue);
				extAnnotation.addMemberValue("message", stringExtMsgValue);
				extAttribute.addAnnotation(extAnnotation);
			}
			
			stringDescValue.setValue(desc);
			descAnnotation.addMemberValue("description", stringDescValue);
			descAttribute.addAnnotation(descAnnotation);
			
			try {
			    cc.getDeclaredField(name);
			    System.out.println(name + " is already defined");
			}
			// the fields have not been added to the class, so add them
			catch (NotFoundException e) {
				CtField field = null;
								
				if (type.equals(STRING_TYPE)) 
					field = CtField.make("java.lang.String " + name + ";", cc);
				else if (type.equals(BOOLEAN_TYPE))
					field = new CtField(CtClass.booleanType, name, cc);
				else if (type.equals(INTEGER_TYPE))
					field = new CtField(CtClass.intType, name, cc);		
				
				
				// add the description annotation to the field
				field.getFieldInfo().addAttribute(descAttribute);
				
				if (constraint != null) {
					// add the constraint annotation to the field
					field.getFieldInfo().addAttribute(extAttribute);
				}
				
				// add the field
				cc.addField(field);			
				
				// create the getters and setters
				CtMethod setMethod = CtNewMethod.setter("set" + name, field);
				CtMethod getMethod = CtNewMethod.getter("get" + name, field);
				cc.addMethod(setMethod);
				cc.addMethod(getMethod);
				
				System.out.println(name + " added successfully");
			}
		}
		// finally, write the modified .class file to the target folder
		cc.writeFile(System.getProperty("user.dir").concat("/target/classes/"));
	}
}
