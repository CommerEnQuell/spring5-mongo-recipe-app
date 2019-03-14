package guru.springframework.recipe.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.converters.IngredientCommandToIngredient;
import guru.springframework.recipe.converters.IngredientToIngredientCommand;
import guru.springframework.recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.IngredientRepository;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.UnitOfMeasureRepository;

class IngredientServiceImplTest {
	
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	
	@Mock
	RecipeRepository recipeRepository;
	
	@Mock
	IngredientRepository ingredientRepository;
	
	@Mock
	UnitOfMeasureRepository unitOfMeasureRepository;
	
	IngredientService ingredientService;
	
	public IngredientServiceImplTest() {
		this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
		this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, recipeRepository, ingredientRepository, ingredientCommandToIngredient, unitOfMeasureRepository);
	}
	
	@Test
	public void findByRecipeIdAndId() throws Exception {
	}

	@Test
	public void findByRecipeIdAndIdHappyPath() throws Exception {
		String id_1 = "1";
		String id_2 = "2";
		String id_3 = "3";
		
		// given
		Recipe recipe = new Recipe();
		recipe.setId(id_1);
			
		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId(id_1);
		
		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId(id_1);
		
		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId(id_3);
		
		recipe.addIngredient(ingredient1);
		recipe.addIngredient(ingredient2);
		recipe.addIngredient(ingredient3);
		Optional<Recipe> recipeOptional = Optional.of(recipe);
		
		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		
		// when
		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(id_1, id_3);
		
		// then
		assertEquals(id_3, ingredientCommand.getId());
		assertEquals(id_1, ingredientCommand.getRecipeId());
		verify(recipeRepository, times(1)).findById(anyString());
	}

	@Test
	public void testSaveRecipeCommand() throws Exception {
		String recipeId = "2";
		String ingrId = "3";
		
		// given
		IngredientCommand command = new IngredientCommand();
		command.setId(ingrId);
		command.setRecipeId(recipeId);
		
		Optional<Recipe> recipeOptional = Optional.ofNullable(new Recipe());
		
		Recipe savedRecipe = new Recipe();
		savedRecipe.addIngredient(new Ingredient());
		savedRecipe.getIngredients().iterator().next().setId(ingrId);
		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		when(recipeRepository.save(any())).thenReturn(savedRecipe);
		
		// when
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
		
		// then
		assertEquals(ingrId, savedCommand.getId());
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, times(1)).save(any(Recipe.class));
	}
	
	@Test
	public void testDeleteById() throws Exception {
		String recipeId = "1";
		String ingrId = "3";
		// given
		Recipe recipe = new Recipe();
		recipe.setId(recipeId);
		Ingredient ingredient = new Ingredient();
		ingredient.setId(ingrId);
		recipe.addIngredient(ingredient);
		ingredient.setRecipe(recipe);
		Optional<Recipe> recipeOptional = Optional.of(recipe);
		
		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		
		// when
		ingredientService.deleteById(recipeId, ingrId);
		
		// then
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, times(1)).save(any(Recipe.class));
	}

}
