package org.openhds.domain.extensions;

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
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

/**
 * This class is automatically called by maven during the compile phase. The idea behind this is to load 
 * a configuration file which contains all of the physical data extensions to be made on the core 
 * entities. Javassist is used to create those extensions by modifying the Java bytecode. More info
 * on Javassist can be found here: http://www.csg.is.titech.ac.jp/~chiba/javassist/.
 * 
 * @author Brian
 */

public class ExtensionLoader {
	
	static BufferedReader input;
	static final String STRING_TYPE = "String";
	static final String BOOLEAN_TYPE = "Boolean";
	static final String INTEGER_TYPE = "Integer";
	static final String NONE_TYPE = "none";
	
	static JCodeModel jCodeModel = null;

	/**
	 * Loads the configuration file and proceeds to walk the document.
	 * 
	 * @param args
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
				
			// load the configuration file
			String currentDirectory = System.getProperty("user.dir");
			if (!currentDirectory.endsWith("domain")) {
				currentDirectory += File.separator + "domain";
				
			}

			File configFile = new File(currentDirectory.concat("/src/main/resources/extension-config.xml"));
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
	 */
	public static void processDocument(Document doc) throws URISyntaxException, IOException, RuntimeException {

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
		
			processAttributes(attributesList, stream, name);
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
	 */
	public static void processAttributes(NodeList attributesList, InputStream stream, String entity) throws IOException, RuntimeException {
		
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
			if (type.equals(STRING_TYPE) || type.equals(INTEGER_TYPE))
				constraint = element.getElementsByTagName("constraint").item(0).getChildNodes().item(0).getNodeValue();
				
			attrMap.put("entity", entity);
			attrMap.put("type", type);
			attrMap.put("description", description);
			
			if ((type.equals(STRING_TYPE) && !constraint.equals(NONE_TYPE)) ||
				(type.equals(INTEGER_TYPE) && !constraint.equals(NONE_TYPE))) 
				attrMap.put("constraint", constraint);
			
			listAttributes.put(name, attrMap);		
		}
		// pass this on to be handled by codemodel
		modifyCode(stream, listAttributes, entity);
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
	public static void modifyCode(InputStream stream, Map<String, Map<String, String>> map, String entityName) {
				
		try {
			
			jCodeModel = new JCodeModel();	
			
			LocationTemplateBuilder locationTemplateBuilder = new LocationTemplateBuilder(jCodeModel);
			IndividualTemplateBuilder individualTemplateBuilder = new IndividualTemplateBuilder(jCodeModel);
			
			JPackage jp = jCodeModel._package("org.openhds.domain.model");					
			JDefinedClass jc = null;
				
			Set<String> keys = map.keySet();
			
			if (keys.size() == 0) {
				if (entityName.equals("Location")) {
					jc = jp._class(entityName);
					locationTemplateBuilder.buildTemplate(jc);
				}
				else if (entityName.equals("Individual")) {
					jc = jp._class(entityName);
					individualTemplateBuilder.buildTemplate(jc);
				}
			}
			
			Iterator<String> keysItr = keys.iterator();
	
			while (keysItr.hasNext()) {
				
				String attribute = keysItr.next();
				String formattedAttr = attribute.substring(0, 1).toUpperCase() + attribute.substring(1).toLowerCase();
				
				String entity = map.get(attribute).get("entity");
				String type = map.get(attribute).get("type");
				String desc = map.get(attribute).get("description");
				
				String constraint = null;
				if (map.get(attribute).containsKey("constraint")) {
					constraint = map.get(attribute).get("constraint");
				}
				
				if (jc == null)
					jc = jp._class(entity);
				
				if (entity.equals("Location") && locationTemplateBuilder.locationTemplateBuilt == false)
					locationTemplateBuilder.buildTemplate(jc);
				else if (entity.equals("Individual") && individualTemplateBuilder.individualTemplateBuilt == false)
					individualTemplateBuilder.buildTemplate(jc);
				
				// build extended fields
				JFieldVar jf = jc.field(JMod.NONE , 			
					(type.equals("String") ? java.lang.String.class :
					type.equals("Integer") ? java.lang.Integer.class :
					type.equals("Boolean") ? java.lang.Boolean.class :
					java.lang.Float.class), attribute);
				JAnnotationUse jaDesc = jf.annotate(org.openhds.domain.annotations.Description.class);
				jaDesc.param("description", desc);	
				
				// determine constraint info
				if ((type.equals("String")) && constraint != null) {
					JAnnotationUse ja = jf.annotate(org.openhds.domain.extensions.ExtensionStringConstraint.class);
					ja.param("constraint", constraint);
					ja.param("message", "Invalid Value for " + attribute);
					ja.param("allowNull", true);
				}
				else if ((type.equals("Integer")) && constraint != null) {
					JAnnotationUse ja = jf.annotate(org.openhds.domain.extensions.ExtensionIntegerConstraint.class);
					ja.param("constraint", constraint);
					ja.param("message", "Invalid Value for " + attribute);
				}

				// getters
				String methodGetName = "get" + formattedAttr;
				JMethod jmg = jc.method(JMod.PUBLIC, 
					(type.equals("String") ? java.lang.String.class :
					type.equals("Integer") ? java.lang.Integer.class :
					type.equals("Boolean") ? java.lang.Boolean.class :
					java.lang.Float.class), methodGetName);
				JBlock jmgBlock = jmg.body();
				jmgBlock._return(jf);
				
				// setters
				String methodSetName = "set" + formattedAttr;
				JMethod jms = jc.method(JMod.PUBLIC, void.class, methodSetName);
				JVar jvar = jms.param(
					(type.equals("String") ? java.lang.String.class :
					type.equals("Integer") ? java.lang.Integer.class :
					type.equals("Boolean") ? java.lang.Boolean.class :
					java.lang.Float.class), "data");
				JBlock jmsBlock = jms.body();
				jmsBlock.assign(jf, jvar);
				
			}
			
			String currentDirectory = System.getProperty("user.dir");
			if (currentDirectory.endsWith("domain")) {
				jCodeModel.build(new File("src/main/java"));					
			} else {
				jCodeModel.build(new File("domain/src/main/java"));
			}
		
		} catch (Exception e) { }	
	}
	
	// Method to get JType based on any String Value
    public static JType getTypeDetailsForCodeModel(JCodeModel jCodeModel, String type) {
        if (type.equals("Short")) {
            return jCodeModel.SHORT;
        } else if (type.equals("Double")) {
            return jCodeModel.DOUBLE;
        } else if (type.equals("Integer")) {
            return jCodeModel.INT;
        } else if (type.equals("Boolean")) {
            return jCodeModel.BOOLEAN;
        } else if (type.equals("Float")) {
            return jCodeModel.FLOAT;    
        } else {
            return null;
        }
    }
}
