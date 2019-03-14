package guru.springframework.recipe.controllers;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {
	
	private final RecipeService recipeService;
	
	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping({"", "/", "/index"})
	public String getIndexPage(Model theModel) {
		
		Set<Recipe> recipes = recipeService.findAll();
		theModel.addAttribute("recipes", recipes);
		
		log.debug("Assuming control: " + recipes.size() + " recipes on display on the main index page");
		
		return "index";
	}

}
