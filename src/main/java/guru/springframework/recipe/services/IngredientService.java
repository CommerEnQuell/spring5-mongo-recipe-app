package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;

public interface IngredientService {
	
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String id);
	public IngredientCommand saveIngredientCommand(IngredientCommand command);
	public void deleteById(String recipeId, String idToDelete);
}
