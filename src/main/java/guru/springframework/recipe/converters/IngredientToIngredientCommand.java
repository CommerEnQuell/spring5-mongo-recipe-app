package guru.springframework.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import lombok.Synchronized;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {
	
	private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;
	
	public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
		this.uomConverter = uomConverter;
	}
	
	@Synchronized
	@Nullable
	@Override
	public IngredientCommand convert(Ingredient source) {
		if (source == null) {
			return null;
		}
		
		IngredientCommand dest = new IngredientCommand();
		dest.setId(source.getId());
//		dest.setRecipeId(source.getRecipe() != null ? source.getRecipe().getId() : null);
		dest.setAmount(source.getAmount());
		dest.setDescription(source.getDescription());
		dest.setUom(uomConverter.convert(source.getUom()));
		return dest;
	}
}
