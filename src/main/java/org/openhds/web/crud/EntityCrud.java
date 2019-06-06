package org.openhds.web.crud;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.openhds.dao.Dao;
import org.openhds.web.ui.PagingState;

/**
 * Interface that represents the actions the user can take when using
 * the front end for a specific entity (i.e. Location)
 * 
 * This interface acts as a controller between the front end and the back end
 *
 * @param <T> The type of entity
 * @param <PK> The primary key type (i.e. java.lang.Long) for a given entity type
 */
public interface EntityCrud<T, PK extends Serializable> {

    void setDao(Dao<T, PK> dao);

    PagingState getPager();

    /**
     * Action to direct user to the full list of entities for a given entity type
     * @return
     */
    String listSetup();

    /**
     * Action to direct user to the form to create a new entity
     * @return
     */
    String createSetup();

    /**
     * Action that is called when user creates a new entity
     * @return
     */
    String create() ;

    /**
     * Action to direct user to the form displaying details for an entity
     * @return
     */
    String detailSetup();

    /**
     * Action to direct user to the edit form for an entity
     * @return
     */
    String editSetup();

    /**
     * Action that is called when user saves an entity from the edit entity form
     * @return
     */
    String edit();

    /**
     * Action that is called when user wants to delete an entity
     * @return
     */
    String delete() ;

    String next();

    String prev();

    /**
     * Get the current entity item
     * Used mostly inside the JSF pages when binding a component to a value
     * @return
     */
    T getItem();

    List<T> getPagedItems();

    SelectItem[] getSelectItems();

    void setConverter(Converter converter);

    Converter getConverter();

    void validateCreate(FacesContext facesContext, UIComponent component, Object value);

    public void setItem(T entityItem);

    void performAudit(T entityItem);
    
    String search();
    
    String clearSearch();
}
