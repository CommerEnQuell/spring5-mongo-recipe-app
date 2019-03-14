package guru.springframework.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.domain.Category;
import lombok.Synchronized;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {
	
	@Synchronized
	@Nullable
	@Override
	public Category convert(CategoryCommand source) {
		if (source == null) {
			return null;
		}
		
		Category category = new Category();
		
		category.setId(source.getId());
		category.setDescription(source.getDescription());
		
		return category;
		
	}

}