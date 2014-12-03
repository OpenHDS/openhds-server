package org.openhds.web.beans;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;

public class LocaleBean {

    private Locale locale;
    private Map<String,Object> allLanguages;
    
    public LocaleBean(){
		allLanguages = new LinkedHashMap<String,Object>();
    }
    	
	public Map<String,Object> getAllLanguages(){
    	if(allLanguages.isEmpty()){
    		initAllLanguages();
    	}		
		return allLanguages;
	}
	
	private void initAllLanguages(){
		Locale defaultLocale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
		allLanguages.put(defaultLocale.getDisplayName(), defaultLocale.getLanguage());
		
		for (Iterator<Locale> iter = FacesContext.getCurrentInstance().getApplication().getSupportedLocales(); iter.hasNext();) 
		{
			Locale locale = iter.next();
			allLanguages.put(locale.getDisplayName(), locale.getLanguage());
		}
	}
    
    private void initLocale() {
    	if(FacesContext.getCurrentInstance() != null)
    		locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    	else
    		locale = new Locale("en");
    }

    public Locale getLocale() {
    	if(locale == null){
    		initLocale();
    	}
        return locale;
    }
    
    public String getLanguage() {
        return locale==null?"":locale.getLanguage();
    }
    
    public void setLanguage(String language) {
        this.locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
    
    public String getLanguageDisplayName() {
        return locale==null?"":locale.getDisplayName();
    }
}