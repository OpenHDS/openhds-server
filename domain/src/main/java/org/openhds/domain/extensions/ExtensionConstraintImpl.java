package org.openhds.domain.extensions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.openhds.domain.constraint.AppContextAware;
import org.openhds.domain.extensions.ValueConstraintService;

public class ExtensionConstraintImpl extends AppContextAware implements ConstraintValidator<ExtensionConstraint, String> {

	private String constraint;
	private boolean allowNull;
	private ValueConstraintService service;

	public void initialize(ExtensionConstraint arg0) {
		service = (ValueConstraintService)context.getBean("valueConstraintService");
		this.constraint = arg0.constraint();
		this.allowNull = arg0.allowNull();
	}

	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		
		if (arg0 == null)
			return true;
		
		if (allowNull == true && arg0.equals(""))
			return true;
		
		return service.isValidConstraintValue(constraint, arg0);
	}
}

