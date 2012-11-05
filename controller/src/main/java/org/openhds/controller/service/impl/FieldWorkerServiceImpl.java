package org.openhds.controller.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openhds.controller.exception.ConstraintViolations;
import org.openhds.controller.idgeneration.FieldWorkerGenerator;
import org.openhds.controller.idgeneration.Generator;
import org.openhds.controller.service.FieldWorkerService;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.annotations.Authorized;
import org.openhds.domain.model.FieldWorker;

@SuppressWarnings("unchecked")
public class FieldWorkerServiceImpl implements FieldWorkerService {

    private GenericDao genericDao;
	private Generator generator;
	
	public FieldWorkerServiceImpl() { }
	
	public FieldWorkerServiceImpl(GenericDao genericDao, Generator generator) {
		this.genericDao = genericDao;
		this.generator = generator;
	}

	public FieldWorker evaluateFieldWorker(FieldWorker entityItem) throws ConstraintViolations { 	
		
		if (findFieldWorkerById(entityItem.getExtId()) != null)
			throw new ConstraintViolations("The Id specified already exists");	
		
		return generateId(entityItem);
    }
		
	public FieldWorker generateId(FieldWorker entityItem) throws ConstraintViolations {
		
		FieldWorkerGenerator fwGen = (FieldWorkerGenerator)generator;
		
		if (fwGen.generated)
			entityItem.setExtId(generator.generateId(entityItem));
		return entityItem;
	}
    
    /**
     * Retrieves all Field Worker extId's that contain the term provided.
     * Used in performing autocomplete.
     */
    public List<String> getFieldWorkerExtIds(String term) {
        List<String> ids = new ArrayList<String>();
        List<FieldWorker> list = genericDao.findListByPropertyPrefix(FieldWorker.class, "extId", term, 10, true);
        for (FieldWorker fw : list) {
            ids.add(fw.getExtId());
        }

        return ids;
    }
    
    public FieldWorker findFieldWorkerById(String fieldWorkerId, String msg) throws Exception {
        FieldWorker fw = genericDao.findByProperty(FieldWorker.class, "extId", fieldWorkerId);
        if (fw == null) {
            throw new Exception(msg);
        }

        return fw;
    }
    
	public FieldWorker findFieldWorkerById(String fieldWorkerId) throws ConstraintViolations {
		 FieldWorker fw = genericDao.findByProperty(FieldWorker.class, "extId", fieldWorkerId);
		 return fw;
	}

	@Override
	public List<FieldWorker> getAllFieldWorkers() {
		return genericDao.findAll(FieldWorker.class, true);
	}
}
