package org.openhds.domain.extensions;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.openhds.domain.constraint.AppContextAware;

public class ExtensionIntegerConstraintImpl extends AppContextAware implements ConstraintValidator<ExtensionIntegerConstraint, Integer> {

	private String constraint;
	private ValueConstraintService service;

	public void initialize(ExtensionIntegerConstraint arg0) {
		service = (ValueConstraintService)context.getBean("valueConstraintService");
		this.constraint = arg0.constraint();
	}

	public boolean isValid(Integer arg0, ConstraintValidatorContext arg1) {
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
