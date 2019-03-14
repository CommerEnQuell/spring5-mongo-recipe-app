package guru.springframework.recipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import guru.springframework.recipe.converters.RecipeCommandToRecipe;
import guru.springframework.recipe.converters.RecipeToRecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.repositories.RecipeRepository;

public class RecipeServiceImplTest {

	
	RecipeServiceImpl recipeService;
	
	@Autowired
	RecipeCommandToRecipe recipeCommandToRecipe;

	@Autowired
	RecipeToRecipeCommand recipeToRecipeCommand;
	
	@Mock
	RecipeRepository recipeRepository;

	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);

		recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
	}

	@Test
	public void getRecipes() throws Exception {
		
		Recipe recipe = new Recipe();
		HashSet<Recipe> recipesData = new HashSet<>();
		recipesData.add(recipe);
		
		Mockito.when(recipeService.getRecipes()).thenReturn(recipesData);

		Set<Recipe> recipes = recipeService.getRecipes();
		
		assertEquals(recipes.size(), 1);
		Mockito.verify(recipeRepository, Mockito.times(1)).findAll();
	}
	
	@Test
	public void testDeleteById() throws Exception {
		
		// given
		String idToDelete = "2";
		
		// when 
		recipeService.deleteById(idToDelete);
		
		// No 'when(..).thenReturn(..)' since method has void return type
		
		// then
		Mockito.verify(recipeRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyString());
	}

	@Test
	public void getRecipeByIdTestNotFound() throws Exception {
		Optional<Recipe> recipeOptional = Optional.empty();
	
//		assertThrows(NotFoundException.class, () -> when(recipeService.findById(ArgumentMatchers.anyString())).thenReturn(recipeOptional));
		
		boolean thrown = false;
		try {
			when(recipeService.findById(ArgumentMatchers.anyString())).thenReturn(recipeOptional);
		} catch (NotFoundException nfx) {
			thrown = true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		if (!thrown) {
			fail("Assertion failed: NotFoundException not thrown");
		}
		
	}
}
