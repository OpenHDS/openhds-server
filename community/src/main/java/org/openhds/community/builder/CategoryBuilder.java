package org.openhds.community.builder;

import java.util.Map;
import java.util.Set;
import org.dhis2.ns.schema.dxf2.CategoriesDocument.Categories;
import org.dhis2.ns.schema.dxf2.CategoryComboDocument.CategoryCombo;
import org.dhis2.ns.schema.dxf2.CategoryCombosDocument.CategoryCombos;
import org.dhis2.ns.schema.dxf2.CategoryDocument.Category;
import org.dhis2.ns.schema.dxf2.CategoryOptionComboDocument.CategoryOptionCombo;
import org.dhis2.ns.schema.dxf2.CategoryOptionCombosDocument.CategoryOptionCombos;
import org.dhis2.ns.schema.dxf2.CategoryOptionsDocument.CategoryOptions;
import org.dhis2.ns.schema.dxf2.IdentifiableObject;
import org.dhis2.ns.schema.dxf2.MetadataDocument.Metadata;

/**
 * Builds the Categories section of the DHIS2 schema.
 * 
 * @author Brian
 */
public class CategoryBuilder {
	
	public void buildCategory(Metadata metadata, Map<String, String> options, String name, String desc, int id) {
		
		Categories categories = metadata.addNewCategories();
		Category category = categories.addNewCategory();
		
		category.setId(id);
		category.setUuid("");
		category.setName(name);
		category.setAlternativeName(name);
		category.setShortName(name);
		category.setCode(name);
		category.setDescription(desc);
				
		Set<String> keySet = options.keySet();
		String[] array = keySet.toArray(new String[0]);
		
		for (int i = 0; i < options.size(); i++) {
			CategoryOptions catOptions = category.addNewCategoryOptions();
			IdentifiableObject catOption = catOptions.addNewCategoryOption();
			
			String value = options.get(array[i]);
			
			catOption.setUuid("");
			catOption.setId(i);
			catOption.setName(value);
			catOption.setAlternativeName(value);
			catOption.setShortName(value);
			catOption.setCode(array[i]);
			catOption.setDescription("");
		}
	}
	
	public void buildDefaultCategoryCombos(Metadata metadata) {
		CategoryCombos categoryCombos = metadata.addNewCategoryCombos();
		CategoryCombo categoryCombo = categoryCombos.addNewCategoryCombo();
		
		categoryCombo.setId(1);
		categoryCombo.setName("default");
	}
	
	public void buildDefaultCategoryOptions(Metadata metadata) {
		CategoryOptions categoryOptions = metadata.addNewCategoryOptions();
		IdentifiableObject categoryOption = categoryOptions.addNewCategoryOption();
		
		categoryOption.setId(1);
		categoryOption.setName("default");
	}
	
	public void buildDefaultCategoryOptionCombos(Metadata metadata) {
		
		CategoryOptionCombos categoryOptionCombos = metadata.addNewCategoryOptionCombos();
		CategoryOptionCombo categoryOptionCombo = categoryOptionCombos.addNewCategoryOptionCombo();
		
		categoryOptionCombo.setId(1);
		categoryOptionCombo.setName("default");
		
		CategoryCombo categoryCombo = categoryOptionCombo.addNewCategoryCombo();
		categoryCombo.setId(1);
		categoryCombo.setName("default");
		
		CategoryOptions categoryOptions = categoryOptionCombo.addNewCategoryOptions();
		IdentifiableObject categoryOption = categoryOptions.addNewCategoryOption();
		categoryOption.setId(1);
		categoryOption.setName("default");	
	}
}
