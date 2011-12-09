package org.openhds.specialstudy.domain;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import org.openhds.specialstudy.domain.Individual;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect IndividualDataOnDemand_Roo_DataOnDemand {
    
    declare @type: IndividualDataOnDemand: @Component;
    
    private Random IndividualDataOnDemand.rnd = new SecureRandom();
    
    private List<Individual> IndividualDataOnDemand.data;
    
    public Individual IndividualDataOnDemand.getNewTransientIndividual(int index) {
        org.openhds.specialstudy.domain.Individual obj = new org.openhds.specialstudy.domain.Individual();
        obj.setExtId("extId_" + index);
        return obj;
    }
    
    public Individual IndividualDataOnDemand.getSpecificIndividual(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size()-1)) index = data.size() - 1;
        Individual obj = data.get(index);
        return Individual.findIndividual(obj.getId());
    }
    
    public Individual IndividualDataOnDemand.getRandomIndividual() {
        init();
        Individual obj = data.get(rnd.nextInt(data.size()));
        return Individual.findIndividual(obj.getId());
    }
    
    public boolean IndividualDataOnDemand.modifyIndividual(Individual obj) {
        return false;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void IndividualDataOnDemand.init() {
        if (data != null) {
            return;
        }
        
        data = org.openhds.specialstudy.domain.Individual.findIndividualEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Individual' illegally returned null");
        if (data.size() > 0) {
            return;
        }
        
        data = new java.util.ArrayList<org.openhds.specialstudy.domain.Individual>();
        for (int i = 0; i < 10; i++) {
            org.openhds.specialstudy.domain.Individual obj = getNewTransientIndividual(i);
            obj.persist();
            data.add(obj);
        }
    }
    
}
