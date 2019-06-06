package org.openhds.web.crud.impl;

import org.openhds.domain.Whitelist;

public class WhitelistCrudImpl extends EntityCrudImpl<Whitelist, String> {

	public WhitelistCrudImpl(Class<Whitelist> entityClass) {
		super(entityClass);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String createSetup() {
        reset(false, true);
        showListing=false;
        entityItem = newInstance();
        navMenuBean.setNextItem(entityClass.getSimpleName());
        navMenuBean.addCrumb(entityClass.getSimpleName() + " Create");
        return outcomePrefix + "_create";
    }


}
