package guru.springframework.recipe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.services.IngredientService;
import guru.springframework.recipe.services.RecipeService;
import guru.springframework.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IngredientController {
	
	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;
	
	public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}
	
	@GetMapping("recipe/{id}/ingredients")
	public String listIngredients(@PathVariable String id, Model model) {
		log.debug("Getting ingredients for recipe #" + id);
		RecipeCommand command = recipeService.findCommandById(id);
		model.addAttribute("recipe", command);
		
		return "recipe/ingredient/list";
	}
	
	@GetMapping("recipe/{recipeId}/ingredient/new")
	public String newRecipe(@PathVariable String recipeId, Model model) {
		// Make sure we have a good id value
		RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
		// TODO Raise exception if null
		
		// Need to return back parent id for hidden form property
		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(recipeId);
		model.addAttribute("ingredient", ingredientCommand);
		// Init uom

		ingredientCommand.setUom(new UnitOfMeasureCommand());
		
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
		
		return "recipe/ingredient/ingredientform";
	}
	
	@GetMapping("recipe/{recipeId}/ingredient/{id}/show")
	public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
		return "recipe/ingredient/show";
	}
	
	@GetMapping("recipe/{recipeId}/ingredient/{id}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
		return "recipe/ingredient/ingredientform";
	}
	
	@PostMapping("recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
		
		log.debug("Saved recipe id: " + savedCommand.getRecipeId());
		log.debug("Saved ingredient id:" + savedCommand.getId());
		
		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show"; 
	}
	
	@GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
	public String delete(@PathVariable String recipeId, @PathVariable String id, Model model) {
		ingredientService.deleteById(recipeId, id);
		return "redirect:/recipe/" + recipeId + "/ingredients";
	}
}
