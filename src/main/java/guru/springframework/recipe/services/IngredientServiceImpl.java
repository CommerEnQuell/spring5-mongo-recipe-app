package guru.springframework.recipe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.converters.IngredientCommandToIngredient;
import guru.springframework.recipe.converters.IngredientToIngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.IngredientRepository;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final UnitOfMeasureRepository unitOfMeasureRepository;
	

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository,
								 IngredientRepository ingredientRepository, IngredientCommandToIngredient ingredientCommandToIngredient,
								 UnitOfMeasureRepository unitOfMeasureRepository) {
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		if (!recipeOptional.isPresent()) {
			//TODO impl error handling
			log.error("Recipe not found. Id=" + recipeId);
		}
		
		Recipe recipe = recipeOptional.get();
		Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientId))
				.map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();
		
		if (!ingredientCommandOptional.isPresent()) {
			//TODO Impl error handling
			log.error("Ingredient not found. Id=" + ingredientId);
		}
		
		return ingredientCommandOptional.get();
	}
	
	@Override
	@Transactional
	public IngredientCommand saveIngredientCommand(IngredientCommand command) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());
		
		if (!recipeOptional.isPresent()) {
			log.error("Recipe not found for if: " + command.getRecipeId());
			return new IngredientCommand();
		}
		
		Recipe recipe = recipeOptional.get();
		
		Optional<Ingredient> ingredientOptional =
				recipe.getIngredients()
					  .stream()
					  .filter(ingredient -> ingredient.getId().equals(command.getId()))
					  .findFirst();
		
		if (ingredientOptional.isPresent()) {
			Ingredient ingredientFound = ingredientOptional.get();
			ingredientFound.setDescription(command.getDescription());
			ingredientFound.setAmount(command.getAmount());
			ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId()).orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));	// TODO address this
		} else {
			// add new ingredient
			Ingredient ingredient = ingredientCommandToIngredient.convert(command);
			ingredient.setRecipe(recipe);
			recipe.addIngredient(ingredient);
		}
		
		Recipe savedRecipe = recipeRepository.save(recipe);
		
		Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
				.filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
				.findFirst();
		
		// Check by description
		if (!savedIngredientOptional.isPresent()) {
			// Not totally safe..., but best guess
			savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription()))
					.filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
					.filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(command.getUom().getId()))
					.findFirst();
		}
		// TODO Check for fail
		return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
	}
	
	@Override
	public void deleteById(String recipeId, String idToDelete) {
		log.debug("Deleting ingredient " + recipeId + ":" + idToDelete);
		
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		if (recipeOptional.isPresent()) {
			Recipe recipe = recipeOptional.get();
			log.debug("Found recipe");
		
			Optional<Ingredient> ingredientOptional =
				recipe.getIngredients().stream()
						.filter(recipeIngredient -> recipeIngredient.getId().equals(idToDelete))
						.findFirst();
			
			if (ingredientOptional.isPresent()) {
				log.debug("Ingredient found");
				Ingredient ingredient = ingredientOptional.get();
				ingredient.setRecipe(null);	// Causes Hibernate to delete the ingredient
				recipe.getIngredients().remove(ingredientOptional.get());
				recipeRepository.save(recipe);
			}
		} else {
			log.warn("Recipe not found (id=" + recipeId + ")");
		}
	}
}
