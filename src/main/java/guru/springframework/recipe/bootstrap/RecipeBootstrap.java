package guru.springframework.recipe.bootstrap;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.domain.Difficulty;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Notes;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.domain.UnitOfMeasure;
import guru.springframework.recipe.repositories.IngredientRepository;
import guru.springframework.recipe.services.CategoryService;
import guru.springframework.recipe.services.NotesService;
import guru.springframework.recipe.services.RecipeService;
import guru.springframework.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("default")
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {
	private final RecipeService recipeService;
	private final UnitOfMeasureService unitOfMeasureService;
	private final CategoryService categoryService;
	private final NotesService notesService;
	private final IngredientRepository ingredientRepository;
	
	
	public RecipeBootstrap(RecipeService recipeService, UnitOfMeasureService uomService, CategoryService catService, IngredientRepository ingredientRepository, NotesService notesService) {
		this.recipeService = recipeService;
		this.unitOfMeasureService = uomService;
		this.categoryService = catService;
		this.notesService = notesService;
		this.ingredientRepository = ingredientRepository;
		log.debug("Data loader initialized");
}

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		loadCategories();
		loadUom();
		int count = recipeService.findAll().size();
		log.debug(count + " recipe(s) found");
		if (count == 0) {
			log.debug("Loading data");
			loadGuacamole();
			loadSomeChicken();
		}
	}
	
	private Recipe loadGuacamole() {
		Recipe kwakmolen = new Recipe();
		kwakmolen.setPrepTime(Integer.valueOf(10));
		kwakmolen.setCookTime(null);
		kwakmolen.setServings(Integer.valueOf(3));
		kwakmolen.setDifficulty(Difficulty.EASY);
		Category category;
		Optional<Category> optCategory = null;
		try {
			categoryService.findByDescription("Mexican");
		} catch (Exception x) {}
		if (optCategory != null && optCategory.isPresent()) {
			category = optCategory.get();
		} else {
			category = new Category();
			category.setDescription("Mexican");
			category = categoryService.save(category);
		}
		Set<Category> categories = kwakmolen.getCategories();
		if (categories == null) {
			categories = new HashSet<>();
			kwakmolen.setCategories(categories);
		}
		categories.add(category);
		kwakmolen.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
		kwakmolen.setDescription("Perfect Guacamole");
		kwakmolen.setSource("Elise Bauer - simplyrecipes.com");
		
		String notesStr = "The BEST guacamole! So easy to make with ripe avocados, salt, serrano chiles, cilantro and lime. "
						+ "Garnish with red radishes or jicama. Serve with tortilla chips. Watch how to make guacamole - it's easy!";
		Notes notes = new Notes();
		notes.setRecipeNotes(notesStr);
		Notes savedNotes = notesService.save(notes);
		
		String directions =
			  "1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n\n"
			+ "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n\n"
			+ "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" 
			+   "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n"
			+   "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n\n"
			+ "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it (The oxygen in the air causes oxidation which will turn the guacamole brown). Refrigerate until ready to serve.\n"
			+   "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.";
		kwakmolen.setDirections(directions);
		Set<Ingredient> ingredients = new HashSet<>();
		addIngredient(ingredients, BigDecimal.valueOf(2.0d), "Each", "ripe avocados");
		addIngredient(ingredients, BigDecimal.valueOf(0.5d), "Teaspoon", "Kosher salt");
		addIngredient(ingredients, BigDecimal.valueOf(1.0d), "Tablespoon", "fresh lime/lemon juice");
		addIngredient(ingredients, BigDecimal.valueOf(0.25d), "Cup", "minced red or sliced green onion");
		addIngredient(ingredients, BigDecimal.valueOf(1.5d), "Each", "serrano chiles, chopped (no stems or seeds)");
		addIngredient(ingredients, BigDecimal.valueOf(2.0d), "Tablespoon", "cilantro (finely chopped)");
		addIngredient(ingredients, null, "Dash", "freshly grated black pepper");
		addIngredient(ingredients, BigDecimal.valueOf(0.5d), "Each", "ripe tomato, chopped");
		
		Recipe savedRecipe = recipeService.save(kwakmolen);
		savedRecipe.setNotes(savedNotes);
		ingredients.forEach(ingredient -> {
			savedRecipe.addIngredient(ingredient);
			ingredientRepository.save(ingredient);
		});
		savedRecipe.setNotes(savedNotes);
		notesService.save(savedNotes);
		recipeService.save(savedRecipe);

		log.debug("Post-Aztec Kwakmolen: Recipe \"" + savedRecipe.getDescription() + "\" saved as #" + savedRecipe.getId());
		
		return savedRecipe;
	}
	
	private Recipe loadSomeChicken() {
		Recipe soChicken = new Recipe();
		soChicken.setPrepTime(Integer.valueOf(20));
		soChicken.setCookTime(Integer.valueOf(15));
		soChicken.setServings(Integer.valueOf(5));
		soChicken.setDifficulty(Difficulty.EASY);
		Category category;
		Optional<Category> optCategory = categoryService.findByDescription("Mexican");
		if (optCategory != null && optCategory.isPresent()) {
			category = optCategory.get();
		} else {
			category = new Category();
			category.setDescription("Mexican");
			category = categoryService.save(category);
		}
		Set<Category> categories = soChicken.getCategories();
		if (categories == null) {
			categories = new HashSet<>();
			soChicken.setCategories(categories);
		}
		categories.add(category);
		categories.add(categoryService.findByDescription("American").get());
		soChicken.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
		soChicken.setDescription("Spicy Grilled Chicken Tacos Recipe");
		soChicken.setSource("Sally Vargas - simplyrecipes.com");
		
		String notesStr = "Spicy grilled chicken tacos! Quick marinade, then grill. Ready in about 30 minutes. "
						+ "Great for a quick weeknight dinner, backyard cookouts, and tailgate parties.";
		Notes notes = new Notes();
		notes.setRecipeNotes(notesStr);
		Notes savedNotes = notesService.save(notes);
		
		String directions =
			  "1 Prepare a gas or charcoal grill for medium-high, direct heat.\r\n"
			+ "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, "
			+   "garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. "
			+   "Add the chicken to the bowl and toss to coat all over.\n" 
			+   "Set aside to marinate while the grill heats and you prepare the rest of the toppings."
			+ "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part "
			+   "of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" 
			+ "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets "
			+   "of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n"
			+   "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n"
			+ "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, "
			+   "sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.";
			
		soChicken.setDirections(directions);
		Set<Ingredient> ingredients = new HashSet<>();
		addIngredient(ingredients, BigDecimal.valueOf(2.0d), "Tablespoon", "ancho chili powder");
		addIngredient(ingredients, BigDecimal.valueOf(1.0d), "Teaspoon", "dried oregano");
		addIngredient(ingredients, BigDecimal.valueOf(1.0d), "Teaspoon", "dried cumin");
		addIngredient(ingredients, BigDecimal.valueOf(1.0d), "Teaspoon", "sugar");
		addIngredient(ingredients, BigDecimal.valueOf(0.5d), "Teaspoon", "salt");
		addIngredient(ingredients, BigDecimal.valueOf(1.0d), "Clove", "garlic, finely chopped");
		addIngredient(ingredients, BigDecimal.valueOf(1.0d), "Tablespoon", "finely grated orange zest");
		addIngredient(ingredients, BigDecimal.valueOf(3.0d), "Tablespoon", "fresh-squeezed orange juice");
		addIngredient(ingredients, BigDecimal.valueOf(2.0d), "Tablespoon", "olive oil");
		addIngredient(ingredients, BigDecimal.valueOf(8.0d), "Each", "small corn tortillas");
		addIngredient(ingredients, BigDecimal.valueOf(3.0d), "Ounce", "packed baby arugula");
		addIngredient(ingredients, BigDecimal.valueOf(5.0d), "Each", "skinless, boneless chicken thighs");
		addIngredient(ingredients, BigDecimal.valueOf(4.0d), "Each", "radishes, thinly sliced");
		addIngredient(ingredients, BigDecimal.valueOf(0.5d), "Pint", "cherry tomatoes, halved");
		addIngredient(ingredients, BigDecimal.valueOf(0.25d), "Each", "red onion, thinly sliced");
		addIngredient(ingredients, null, null, "cilantro (roughly chopped)");
		addIngredient(ingredients, BigDecimal.valueOf(0.5d), "Cup", "sour cream");
		addIngredient(ingredients, BigDecimal.valueOf(0.25d), "Cup", "milk");
		addIngredient(ingredients, BigDecimal.valueOf(1.0d), "Each", "lime, cut into wedges");
		
		Recipe savedRecipe = recipeService.save(soChicken);
		savedRecipe.setNotes(savedNotes);
		ingredients.forEach(ingredient -> {
			savedRecipe.addIngredient(ingredient);
			ingredientRepository.save(ingredient);
		});
		notesService.save(savedNotes);
		recipeService.save(savedRecipe);

		log.debug("Chickenshit Incident: Recipe \"" + savedRecipe.getDescription() + "\" saved as #" + savedRecipe.getId());
		
		return savedRecipe;
	}

	private void addIngredient(Set<Ingredient> ingredients, BigDecimal amount, String unit, String descr) {
		Ingredient ingredient = new Ingredient();
		ingredient.setAmount(amount);
		ingredient.setDescription(descr);
		
		if (unit != null && unit.length() > 0) {
			UnitOfMeasure uom = unitOfMeasureService.findByDescription(unit);
			ingredient.setUom(uom);
		}
		
		Ingredient savedIngredient = ingredientRepository.save(ingredient);
		
		ingredients.add(savedIngredient);
	}


    private void loadCategories(){
        Category cat1 = new Category();
        cat1.setDescription("American");
        categoryService.save(cat1);

        Category cat2 = new Category();
        cat2.setDescription("Italian");
        categoryService.save(cat2);

        Category cat3 = new Category();
        cat3.setDescription("Mexican");
        categoryService.save(cat3);

        Category cat4 = new Category();
        cat4.setDescription("Fast Food");
        categoryService.save(cat4);
    }



    private void loadUom(){

        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription("Teaspoon");
        unitOfMeasureService.save(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription("Tablespoon");
        unitOfMeasureService.save(uom2);

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription("Cup");
        unitOfMeasureService.save(uom3);

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription("Pinch");
        unitOfMeasureService.save(uom4);

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription("Ounce");
        unitOfMeasureService.save(uom5);

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription("Each");
        unitOfMeasureService.save(uom6);

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription("Pint");
        unitOfMeasureService.save(uom7);

        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription("Dash");
        unitOfMeasureService.save(uom8);

        UnitOfMeasure uom9 = new UnitOfMeasure();
        uom9.setDescription("Clove");
        unitOfMeasureService.save(uom9);
   }


}
