package org.openhds.extensions;

import com.sun.codemodel.JDefinedClass;

public interface ExtensionTemplate {
	
	void buildTemplate(JDefinedClass jc);
	
	void buildFieldsAndMethods(JDefinedClass jc);
	
	void buildClassAnnotations(JDefinedClass jc);
}
