package guru.springframework.recipe.services;

import java.util.Optional;
import java.util.Set;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;

public interface RecipeService {

	public Set<Recipe> findAll();
	
	public Optional<Recipe> findById(String id);

	public RecipeCommand findCommandById(String id);
	
	public Recipe save(Recipe recipe);
	
	public RecipeCommand saveRecipeCommand(RecipeCommand command);

	public void deleteById(String id);

} 
