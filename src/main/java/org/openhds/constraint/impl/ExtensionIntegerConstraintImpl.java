package org.openhds.constraint.impl;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.openhds.constraint.AppContextAware;
import org.openhds.constraint.ExtensionIntegerConstraint;
import org.openhds.service.impl.ValueConstraintServiceImpl;

public class ExtensionIntegerConstraintImpl extends AppContextAware implements ConstraintValidator<ExtensionIntegerConstraint, Integer> {

	private String constraint;
	private ValueConstraintServiceImpl service;
    private boolean allowNull;

	public void initialize(ExtensionIntegerConstraint arg0) {
		service = (ValueConstraintServiceImpl)context.getBean("valueConstraintService");
		this.constraint = arg0.constraint();
		this.allowNull = arg0.allowNull();
	}

	public boolean isValid(Integer arg0, ConstraintValidatorContext arg1) {
		
        if (allowNull && arg0 == null) {
            return true;
        }
		
		Map<String, String> map = service.getMapForConstraint(constraint);
		Set<String> keys = map.keySet();
		
		for (String string : keys) {
			
			try {
				Integer i = Integer.parseInt(string);
				if (i.equals(arg0)) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
}
