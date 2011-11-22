package org.openhds.web.util;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELResolver;
import org.openhds.domain.constraint.AppContextAware;
import org.openhds.domain.value.extension.ValueConstraintService;

public class ValueExtensionResolver extends ELResolver {
	
	ValueConstraintService service;
	
	public ValueExtensionResolver() {			
		service = AppContextAware.getContext().getBean(ValueConstraintService.class);
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return null;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		return null;
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (property.equals("valueExtensionIterator")) {
			context.setPropertyResolved(true);
			return new TagInterface() { };
		}
		else if (base != null && base instanceof TagInterface) {
			String constraintName = property.toString();
			context.setPropertyResolved(true);
			return service.getMapForConstraint(constraintName);
		}
		return null;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return false;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {		
	
	}
	
	interface TagInterface { }
}
