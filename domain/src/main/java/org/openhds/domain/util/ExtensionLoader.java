package org.openhds.domain.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.aspectj.weaver.ast.Expr;
import org.openhds.domain.model.Residency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JExpressionImpl;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

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
	 * @throws CannotCompileException
	 * @throws NotFoundException 
	 */
	public static void processAttributes(NodeList attributesList, InputStream stream, String entity) throws IOException, RuntimeException, CannotCompileException, NotFoundException {
		
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
				
			attrMap.put("entity", entity);
			attrMap.put("type", type);
			attrMap.put("description", description);
			
			if (type.equals(STRING_TYPE) && !constraint.equals(NONE_TYPE)) 
				attrMap.put("constraint", constraint);
			
			listAttributes.put(name, attrMap);		
		}
		// pass this on to be handled by codemodel
		modifyCode(stream, listAttributes);
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
	public static void modifyCode(InputStream stream, Map<String, Map<String, String>> map) {
				
		try {
				
			Set<String> keys = map.keySet();
			
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
				
				jCodeModel = new JCodeModel();		
				JPackage jp = jCodeModel._package("org.openhds.domain.model");					
				JDefinedClass jc = jp._class(entity);
				
				if (entity.equals("Location"))
					buildLocationTemplate(jc);
				
				String methodName = "set" + formattedAttr;
				JMethod jmCreate = jc.method(JMod.PUBLIC, java.lang.String.class, methodName);
				
				JBlock jBlock = jmCreate.body();
	            JType jt = getTypeDetailsForCodeModel(jCodeModel, type);
	             if (jt != null) {
	                 jmCreate.param(jt, "data");
	             } else {
	                jmCreate.param(java.lang.String.class, "data");
	             }
			}
			
			jCodeModel.build(new File("src/main/java"));
		
		} catch (Exception e) { }	
	}
	
	public static void buildLocationTemplate(JDefinedClass jc) {
		
		JDocComment jDocComment = jc.javadoc();
		jDocComment.add("Generated by JCodeModel");
		
		jc._extends(org.openhds.domain.model.AuditableCollectedEntity.class);
		jc._implements(java.io.Serializable.class);
		
		buildLocationClassAnnotations(jc);	
		buildLocationFieldsAndMethods(jc);
	}
	
	public static void buildLocationFieldsAndMethods(JDefinedClass jc) {
			
		// serial uuid
		JFieldVar jfSerial = jc.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, long.class, "serialVersionUID");
		jfSerial.init(JExpr.lit(169551578162260199L));
		
		// extId
		JFieldVar jfExtId = jc.field(JMod.NONE , java.lang.String.class, "extId");
		jfExtId.annotate(javax.validation.constraints.NotNull.class);
		jfExtId.annotate(org.openhds.domain.constraint.CheckFieldNotBlank.class);
		jfExtId.annotate(org.openhds.domain.constraint.Searchable.class);
		JAnnotationUse jaExtIdDesc = jfExtId.annotate(org.openhds.domain.annotations.Description.class);
		jaExtIdDesc.param("description", "External Id of the location. This id is used internally.");
		
		// getter
		JMethod jmgExtId = jc.method(JMod.PUBLIC, java.lang.String.class, "getExtId");
		JBlock jmgExtIdBlock = jmgExtId.body();
		jmgExtIdBlock._return(jfExtId);
		
		// setter
		JMethod jmsExtId = jc.method(JMod.PUBLIC, void.class, "setExtId");
		JVar jvarExtId = jmsExtId.param(java.lang.String.class, "id");
		JBlock jmsExtIdBlock = jmsExtId.body();
		jmsExtIdBlock.assign(jfExtId, jvarExtId);
		
		// location name
		JFieldVar jfLocationName = jc.field(JMod.NONE , java.lang.String.class, "locationName");
		jfLocationName.annotate(org.openhds.domain.constraint.CheckFieldNotBlank.class);
		jfLocationName.annotate(org.openhds.domain.constraint.Searchable.class);
		JAnnotationUse jaLocationNameDesc = jfLocationName.annotate(org.openhds.domain.annotations.Description.class);
		jaLocationNameDesc.param("description", "Name of the location.");
		
		// getter
		JMethod jmgLocationName = jc.method(JMod.PUBLIC, java.lang.String.class, "getLocationName");
		JBlock jmgLocationNameBlock = jmgLocationName.body();
		jmgLocationNameBlock._return(jfLocationName);
		
		// setter
		JMethod jmsLocationName = jc.method(JMod.PUBLIC, void.class, "setLocationName");
		JVar jvarLocationName = jmsLocationName.param(java.lang.String.class, "name");
		JBlock jmsLocationNameBlock = jmsLocationName.body();
		jmsLocationNameBlock.assign(jfLocationName, jvarLocationName);
		
		// location level
		JFieldVar jfLocationLevel = jc.field(JMod.NONE , org.openhds.domain.model.LocationHierarchy.class, "locationLevel");
		JClass jClassRef = jCodeModel.ref(org.openhds.domain.model.LocationHierarchy.class);
		jfLocationLevel.init(JExpr._new(jClassRef));	
		jfLocationLevel.annotate(javax.persistence.ManyToOne.class);
		JAnnotationUse jaLocationLevel = jfLocationLevel.annotate(org.hibernate.annotations.Cascade.class);
		jaLocationLevel.param("value", org.hibernate.annotations.CascadeType.SAVE_UPDATE);
		
		// getter
		JMethod jmgLocationLevel = jc.method(JMod.PUBLIC, org.openhds.domain.model.LocationHierarchy.class, "getLocationLevel");
		JBlock jmgLocationLevelBlock = jmgLocationLevel.body();
		jmgLocationLevelBlock._return(jfLocationLevel);
		
		// setter
		JMethod jmsLocationLevel = jc.method(JMod.PUBLIC, void.class, "setLocationLevel");
		JVar jvarLocationLevel = jmsLocationLevel.param(org.openhds.domain.model.LocationHierarchy.class, "level");
		JBlock jmsLocationLevelBlock = jmsLocationLevel.body();
		jmsLocationLevelBlock.assign(jfLocationLevel, jvarLocationLevel);
		
		// location type
		JFieldVar jfLocationType = jc.field(JMod.NONE , java.lang.String.class, "locationType");
		JAnnotationUse jaLocationType = jfLocationType.annotate(org.openhds.domain.value.extension.ExtensionConstraint.class);
		jaLocationType.param("constraint", "locationTypeConstraint");
		jaLocationType.param("message", "Invalid Value for location type");
		jaLocationType.param("allowNull", true);
		JAnnotationUse jaLocationTypeDesc = jfLocationType.annotate(org.openhds.domain.annotations.Description.class);
		jaLocationTypeDesc.param("description", "The type of Location.");
		
		// getter
		JMethod jmgLocationType = jc.method(JMod.PUBLIC, java.lang.String.class, "getLocationType");
		JBlock jmgLocationTypeBlock = jmgLocationType.body();
		jmgLocationTypeBlock._return(jfLocationType);
		
		// setter
		JMethod jmsLocationType = jc.method(JMod.PUBLIC, void.class, "setLocationType");
		JVar jvarLocationType = jmsLocationType.param(java.lang.String.class, "type");
		JBlock jmsLocationTypeBlock = jmsLocationType.body();
		jmsLocationTypeBlock.assign(jfLocationType, jvarLocationType);
		
		// residencies
		JFieldVar jfResidencies = jc.field(JMod.NONE , java.util.List.class, "residencies");
		JAnnotationUse jaResidenciesTarget = jfResidencies.annotate(javax.persistence.OneToMany.class);
		jaResidenciesTarget.param("targetEntity", org.openhds.domain.model.Residency.class);
		JAnnotationUse jaResidenciesColumn = jfResidencies.annotate(javax.persistence.JoinColumn.class);
		jaResidenciesColumn.param("name", "location_uuid");
		
		// getter
		JMethod jmgLocationResidencies = jc.method(JMod.PUBLIC, java.util.List.class, "getResidencies");
		JBlock jmgLocationResidenciesBlock = jmgLocationResidencies.body();
		jmgLocationResidenciesBlock._return(jfResidencies);
		
		// setter
		JMethod jmsLocationResidencies = jc.method(JMod.PUBLIC, void.class, "setResidencies");
		JVar jvarLocationResidencies = jmsLocationResidencies.param(java.util.List.class, "list");
		JBlock jmsLocationResidenciesBlock = jmsLocationResidencies.body();
		jmsLocationResidenciesBlock.assign(jfResidencies, jvarLocationResidencies);
	}
	
	public static void buildLocationClassAnnotations(JDefinedClass jc) {
		
		// create Description annotation
		JAnnotationUse jad = jc.annotate(org.openhds.domain.annotations.Description.class);
		jad.param("description", "All distinct Locations within the area of study are " +
				"represented here. A Location is identified by a uniquely generated " +
				"identifier that the system uses internally. Each Location has a name associated " +
				"with it and resides at a particular level within the Location Hierarchy.");
		
		// create Entity annotation
		jc.annotate(javax.persistence.Entity.class);
		
		JAnnotationUse jat = jc.annotate(javax.persistence.Table.class);
		jat.param("name", "location");
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
