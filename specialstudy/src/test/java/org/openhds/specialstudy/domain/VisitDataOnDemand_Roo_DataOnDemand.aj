package org.openhds.specialstudy.domain;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import org.openhds.specialstudy.domain.Visit;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect VisitDataOnDemand_Roo_DataOnDemand {
    
    declare @type: VisitDataOnDemand: @Component;
    
    private Random VisitDataOnDemand.rnd = new SecureRandom();
    
    private List<Visit> VisitDataOnDemand.data;
    
    public Visit VisitDataOnDemand.getNewTransientVisit(int index) {
        org.openhds.specialstudy.domain.Visit obj = new org.openhds.specialstudy.domain.Visit();
        obj.setExtId("extId_" + index);
        return obj;
    }
    
    public Visit VisitDataOnDemand.getSpecificVisit(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size()-1)) index = data.size() - 1;
        Visit obj = data.get(index);
        return Visit.findVisit(obj.getId());
    }
    
    public Visit VisitDataOnDemand.getRandomVisit() {
        init();
        Visit obj = data.get(rnd.nextInt(data.size()));
        return Visit.findVisit(obj.getId());
    }
    
    public boolean VisitDataOnDemand.modifyVisit(Visit obj) {
        return false;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void VisitDataOnDemand.init() {
        if (data != null) {
            return;
        }
        
        data = org.openhds.specialstudy.domain.Visit.findVisitEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Visit' illegally returned null");
        if (data.size() > 0) {
            return;
        }
        
        data = new java.util.ArrayList<org.openhds.specialstudy.domain.Visit>();
        for (int i = 0; i < 10; i++) {
            org.openhds.specialstudy.domain.Visit obj = getNewTransientVisit(i);
            obj.persist();
            data.add(obj);
        }
    }
    
}
