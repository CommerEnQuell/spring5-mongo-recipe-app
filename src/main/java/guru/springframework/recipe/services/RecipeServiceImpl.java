package guru.springframework.recipe.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.converters.RecipeCommandToRecipe;
import guru.springframework.recipe.converters.RecipeToRecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class RecipeServiceImpl extends AbstractServiceImpl<Recipe, String> implements RecipeService {
	
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
		super(recipeRepository);
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}
	
	@Override
	public Set<Recipe> findAll() {
		return super.findAll();
	}

	public Set<Recipe> getRecipes() {
		log.debug("I'm in the service");
		
		return super.findAll();
		
	}
	
	@Override
	public Optional<Recipe> findById(String id) {
		log.debug("Finding recipe with id #" + id);
		
		Optional<Recipe> recipeOptional = repository.findById(id);
		if (recipeOptional == null || !recipeOptional.isPresent()) {
			throw new NotFoundException("Recipe not found. For ID value: " + id);
		}
		
		return recipeOptional;
	}
	
	@Override
	@Transactional
	public RecipeCommand findCommandById(String id) {
		Optional<Recipe> recipeOptional = findById(id);
		if (recipeOptional == null || !recipeOptional.isPresent()) {
			throw new NotFoundException("Recipe not found (id=" + id + ")");
		}
		
		RecipeCommand retval = recipeToRecipeCommand.convert(recipeOptional.get());
		return retval;
	}
	
	@Override
	@Transactional
	public RecipeCommand saveRecipeCommand(RecipeCommand command) {
		Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
		
		Recipe savedRecipe = save(detachedRecipe);
		log.debug("Saved recipeId: " + savedRecipe.getId());
		return recipeToRecipeCommand.convert(savedRecipe);
	}
	
	@Override
	public void deleteById(String id) {
		super.deleteById(id);
	}
}
