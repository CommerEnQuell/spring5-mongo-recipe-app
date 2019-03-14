package guru.springframework.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import lombok.Synchronized;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
	
	private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;
	
	public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter) {
		this.uomConverter = uomConverter;
	}
	
	@Synchronized
	@Nullable
	@Override
	public Ingredient convert(IngredientCommand source) {
		if (source == null) {
			return null;
		}
		
		Ingredient dest = new Ingredient();
		dest.setId(source.getId());
		dest.setAmount(source.getAmount());
		dest.setDescription(source.getDescription());
		dest.setUom(uomConverter.convert(source.getUom()));
		return dest;
	}
}
