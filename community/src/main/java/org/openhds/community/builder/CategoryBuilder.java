package org.openhds.community.builder;

import java.util.Map;
import java.util.Set;
import org.dhis2.ns.schema.dxf2.CategoriesDocument.Categories;
import org.dhis2.ns.schema.dxf2.CategoryDocument.Category;
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
}
