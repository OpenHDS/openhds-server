package org.openhds.extensions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openhds.domain.util.CalendarAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
 * This class loads a configuration file which contains all of the physical data extensions to be made on the core 
 * entities. JCodeModel is used to create those extensions by modifying the Java source files. 
 * 
 * @author Brian
 */

public class ExtensionLoader {
	
	BufferedReader input;
	final String STRING_TYPE = "String";
	final String BOOLEAN_TYPE = "Boolean";
	final String INTEGER_TYPE = "Integer";
	final String NONE_TYPE = "none";
	
	static JCodeModel jCodeModel = null;
	
	public ExtensionLoader() { }

	/**
	 * Loads the configuration file and proceeds to walk the document.
	 */
	public boolean start() {
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			File configFile = new File("../domain/src/main/resources/extension-config.xml");
			Document doc = builder.parse(configFile);
			
			return processDocument(doc);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * This method is responsible for walking the xml configuration file.
	 */
	public boolean processDocument(Document doc) throws URISyntaxException, IOException, RuntimeException {

		NodeList items = doc.getElementsByTagName("entity");
		
		for (int i = 0; i < items.getLength(); i++) {
			
			// which entity is this?
			Element entityElement = (Element)items.item(i);			
			String name = entityElement.getAttributes().getNamedItem("name").getNodeValue();
			
			// we must now traverse all attributes specific for the entity
			NodeList attributesList = entityElement.getElementsByTagName("attributes");	
			
			if (attributesList.getLength() == 0)
				continue;
		
			processAttributes(attributesList, name);
		}
		return true;
	}
	
	/**
	 * This method is responsible for obtaining all attributes specific for the entity.
	 * It puts all of the attributes in a map and then passes them on to be modified by
	 * jcodemodel.
	 */
	public void processAttributes(NodeList attributesList, String entity) throws IOException, RuntimeException {
		
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
		// pass this on to be handled by kcodemodel
		modifyCode(listAttributes, entity);
	}
	
	/**
	 * This method makes use of the jcodemodel api and modifies the source files. The fields are
	 * added and their corresponding getters/setters.
	 */
	public void modifyCode(Map<String, Map<String, String>> map, String entityName) {
				
		try {
			
			jCodeModel = new JCodeModel();	
			
			LocationTemplateBuilder locationTemplateBuilder = new LocationTemplateBuilder(jCodeModel);
			IndividualTemplateBuilder individualTemplateBuilder = new IndividualTemplateBuilder(jCodeModel);
			VisitTemplateBuilder visitTemplateBuilder = new VisitTemplateBuilder(jCodeModel);
			SocialGroupTemplateBuilder socialGroupTemplateBuilder = new SocialGroupTemplateBuilder(jCodeModel);
			AdultVPMTemplateBuilder adultVPMTemplateBuilder = new AdultVPMTemplateBuilder(jCodeModel);
			
			
			JPackage jp = jCodeModel._package("org.openhds.domain.model");					
			JDefinedClass jc = null;
				
			Set<String> keys = map.keySet();
			
			if (keys.size() == 0) {
				jc = jp._class(entityName);
				if (entityName.equals("Location")) 
					locationTemplateBuilder.buildTemplate(jc);
				else if (entityName.equals("Individual")) 
					individualTemplateBuilder.buildTemplate(jc);
				else if (entityName.equals("Visit")) 
					visitTemplateBuilder.buildTemplate(jc);
				else if (entityName.equals("SocialGroup")) 
					socialGroupTemplateBuilder.buildTemplate(jc);
				else if (entityName.equals("AdultVPM")) 
					adultVPMTemplateBuilder.buildTemplate(jc);
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
				else if (entity.equals("Visit") && visitTemplateBuilder.visitTemplateBuilt == false)
					visitTemplateBuilder.buildTemplate(jc);
				else if (entity.equals("SocialGroup") && socialGroupTemplateBuilder.socialGroupTemplateBuilt == false)
					socialGroupTemplateBuilder.buildTemplate(jc);
				else if (entity.equals("AdultVPM") && adultVPMTemplateBuilder.templateBuilt == false)
					adultVPMTemplateBuilder.buildTemplate(jc);
				
				// build extended fields
				JFieldVar jf = jc.field(JMod.PRIVATE , 			
					(type.equals("String") ? java.lang.String.class :
					type.equals("Integer") ? java.lang.Integer.class :
					type.equals("Boolean") ? java.lang.Boolean.class :
					type.equals("Calendar") ? java.util.Calendar.class :
					java.lang.Float.class), attribute);
				JAnnotationUse jaDesc = jf.annotate(org.openhds.domain.annotations.Description.class);
				jaDesc.param("description", desc);	
				
				// determine constraint info
				if ((type.equals("String")) && constraint != null) {
					JAnnotationUse ja = jf.annotate(org.openhds.domain.constraint.ExtensionStringConstraint.class);
					ja.param("constraint", constraint);
					ja.param("message", "Invalid Value for " + attribute);
					ja.param("allowNull", true);
				}
				else if ((type.equals("Integer")) && constraint != null) {
					JAnnotationUse ja = jf.annotate(org.openhds.domain.constraint.ExtensionIntegerConstraint.class);
					ja.param("constraint", constraint);
					ja.param("message", "Invalid Value for " + attribute);
					ja.param("allowNull", true);
				}
				else if (type.equals("Calendar")) {
					JAnnotationUse jaTemporal = jf.annotate(javax.persistence.Temporal.class);
					jaTemporal.param("value", javax.persistence.TemporalType.DATE);
					if (constraint.equals("past"))
						jf.annotate(javax.validation.constraints.Past.class);
				}

				// getters
				String methodGetName;
				if (type.equals("Boolean"))
					methodGetName = "is" + formattedAttr;
				else
					methodGetName = "get" + formattedAttr;
				
				JMethod jmg = jc.method(JMod.PUBLIC, 
					(type.equals("String") ? java.lang.String.class :
					type.equals("Integer") ? java.lang.Integer.class :
					type.equals("Boolean") ? java.lang.Boolean.class :
					type.equals("Calendar") ? java.util.Calendar.class :
					java.lang.Float.class), methodGetName);
				
				if (type.equals("Calendar")) {
					JAnnotationUse jaXmlDob = jmg.annotate(javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.class);
					jaXmlDob.param("value", CalendarAdapter.class);
				}
				
				JBlock jmgBlock = jmg.body();
				jmgBlock._return(jf);
				
				// setters
				String methodSetName = "set" + formattedAttr;
				JMethod jms = jc.method(JMod.PUBLIC, void.class, methodSetName);
				JVar jvar = jms.param(
					(type.equals("String") ? java.lang.String.class :
					type.equals("Integer") ? java.lang.Integer.class :
					type.equals("Boolean") ? java.lang.Boolean.class :
					type.equals("Calendar") ? java.util.Calendar.class :
					java.lang.Float.class), "data");
				JBlock jmsBlock = jms.body();
				jmsBlock.assign(jf, jvar);
				
			}

			jCodeModel.build(new File("../domain/src/main/java"));					

		} catch (Exception e) { 
			e.printStackTrace();
		}	
	}
	
	// Method to get JType based on any String Value
    public JType getTypeDetailsForCodeModel(JCodeModel jCodeModel, String type) {
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
