package org.openhds.service.impl;

import java.util.List;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.EntityService;
import org.openhds.service.FormService;
import org.openhds.dao.GenericDao;
import org.openhds.annotations.Authorized;
import org.openhds.domain.Form;

public class FormServiceImpl implements FormService {

    private EntityService entityService;
    private GenericDao genericDao;

    public FormServiceImpl(GenericDao genericDao, EntityService entityService) {
        this.genericDao = genericDao;
        this.setEntityService(entityService);
    }

    public void evaluateForm(Form form) throws ConstraintViolations {
        if (form.getUuid() == null && isDuplicateForm(form)) {
            throw new ConstraintViolations(
                    "A form already exists with that name. Please enter in a unique name.");
        }
    }

    private boolean isDuplicateForm(Form form) {
        Form r = genericDao.findByProperty(Form.class, "formName", form.getFormName());
        return r != null;
    }

    @Override
    public List<Form> getAllActiveForms() {
        return genericDao.findListByProperty(Form.class, "active", "Yes", true);
    	// return genericDao.findAll(Form.class, false);
    }

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}
	
	@Override
    @Authorized("VIEW_ENTITY")
    public long getTotalFormCount() {
        return genericDao.getTotalCount(Form.class);
    }

}
