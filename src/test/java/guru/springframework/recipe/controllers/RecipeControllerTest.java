package guru.springframework.recipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.services.RecipeService;

class RecipeControllerTest {
	@Mock
	RecipeService recipeService;
	
	RecipeController controller;
	
	MockMvc mockMvc;

	@Before
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		controller = new RecipeController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
					.setControllerAdvice(new ControllerExceptionHandler())
					.build();
	}

	@Test
	void testGetRecipe() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId("1");
		
		Optional<Recipe> recipeOptional = Optional.of(recipe);
		
		when(recipeService.findById(anyString())).thenReturn(recipeOptional);

		mockMvc.perform(get("/recipe/1/show"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("recipe/show"))
			   .andExpect(model().attributeExists("recipe"));
	}
	
	@Test
	public void testGetRecipeNotFound() throws Exception {
		when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);
		
		mockMvc.perform(get("/recipe/1/show"))
				.andExpect(status().isNotFound())
				.andExpect(view().name("404error"));
	}
	
	@Test
	public void testGetRecipeNumberFormatException() throws Exception {
		mockMvc.perform(get("/recipe/asdf/show"))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("400error"));
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {
		RecipeCommand command = new RecipeCommand();
		
		mockMvc.perform(get("/recipe/new"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("recipe/recipeform"))
			   .andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("27");
		when(recipeService.saveRecipeCommand(any())).thenReturn(command);
		
		mockMvc.perform(post("/recipe")
			   .contentType(MediaType.APPLICATION_FORM_URLENCODED)
			   .param("id", "")
			   .param("description", "some string")
			   .param("directions", "some directions"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/recipe/27/show"));
	}
	
	@Test
	public void testPostNewRecipeFormValidationFail() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");
		
		when(recipeService.saveRecipeCommand(any())).thenReturn(command);
		
		mockMvc.perform(post("/recipe")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "")
		)		.andExpect(status().isOk())
				.andExpect(model().attributeExists("recipe"))
				.andExpect(view().name("recipe/recipeform"));
		
	}
	
	@Test
	public void testGetUpdateView() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");
		
		when(recipeService.findCommandById(anyString())).thenReturn(command);
		
		mockMvc.perform(get("/recipe/1/update"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("recipe/recipeform"))
			   .andExpect(model().attributeExists("recipe"));
		
	}
	
	@Test
	public void testDeleteAction() throws Exception {
		mockMvc.perform(get("/recipe/1/delete"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/"));
		
		verify(recipeService, times(1)).deleteById(anyString());
	}
}
