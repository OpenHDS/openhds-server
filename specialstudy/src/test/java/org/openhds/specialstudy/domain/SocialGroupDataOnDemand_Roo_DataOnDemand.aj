package org.openhds.specialstudy.domain;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import org.openhds.specialstudy.domain.SocialGroup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SocialGroupDataOnDemand_Roo_DataOnDemand {
    
    declare @type: SocialGroupDataOnDemand: @Component;
    
    private Random SocialGroupDataOnDemand.rnd = new SecureRandom();
    
    private List<SocialGroup> SocialGroupDataOnDemand.data;
    
    public SocialGroup SocialGroupDataOnDemand.getNewTransientSocialGroup(int index) {
        org.openhds.specialstudy.domain.SocialGroup obj = new org.openhds.specialstudy.domain.SocialGroup();
        obj.setExtId("extId_" + index);
        return obj;
    }
    
    public SocialGroup SocialGroupDataOnDemand.getSpecificSocialGroup(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size()-1)) index = data.size() - 1;
        SocialGroup obj = data.get(index);
        return SocialGroup.findSocialGroup(obj.getId());
    }
    
    public SocialGroup SocialGroupDataOnDemand.getRandomSocialGroup() {
        init();
        SocialGroup obj = data.get(rnd.nextInt(data.size()));
        return SocialGroup.findSocialGroup(obj.getId());
    }
    
    public boolean SocialGroupDataOnDemand.modifySocialGroup(SocialGroup obj) {
        return false;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void SocialGroupDataOnDemand.init() {
        if (data != null) {
            return;
        }
        
        data = org.openhds.specialstudy.domain.SocialGroup.findSocialGroupEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'SocialGroup' illegally returned null");
        if (data.size() > 0) {
            return;
        }
        
        data = new java.util.ArrayList<org.openhds.specialstudy.domain.SocialGroup>();
        for (int i = 0; i < 10; i++) {
            org.openhds.specialstudy.domain.SocialGroup obj = getNewTransientSocialGroup(i);
            obj.persist();
            data.add(obj);
        }
    }
    
}
