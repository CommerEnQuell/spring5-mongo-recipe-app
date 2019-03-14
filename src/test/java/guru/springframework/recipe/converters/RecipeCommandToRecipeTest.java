package guru.springframework.recipe.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.domain.Difficulty;
import guru.springframework.recipe.domain.Identifiable;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Notes;
import guru.springframework.recipe.domain.Recipe;

class RecipeCommandToRecipeTest {
	public static final String RECIPE_ID = "1";
	public static final String UOM_ID_1  = "11";
	public static final String UOM_ID_2  = "12";
	public static final String CAT_ID    = "21";
	public static final String ING_ID_1  = "31";
	public static final String ING_ID_2  = "32";
	public static final String ING_ID_3  = "33";
	public static final String NOTES_ID  = "41";

	
	public static final Integer TIME_MIN = Integer.valueOf(10);
	public static final Integer SERVINGS = Integer.valueOf(4);
	
	public static final String LOREM_IPSUM = "Lorem ipsum etc.";
	public static final String HOCUS_POCUS = "Hocus Pocus etc.";
	public static final String HOTUM_FACTOTUM = "Hotum Factotum etc.";
	public static final String EXAMPLE_URL = "http://www.example.com";
	public static final String UOM_1 = "Freightload";
	public static final String UOM_2 = "Pieces";
	public static final String CAT   = "Convertable";
	public static final String ING_1 = "Fried Air";
	public static final String ING_2 = "Hot Potatoes";
	public static final String ING_3 = "Egg Yolk";
	
	
	RecipeCommandToRecipe converter;

	@Before
	void setUp() throws Exception {
		converter = new RecipeCommandToRecipe(
						new CategoryCommandToCategory(),
						new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
						new NotesCommandToNotes());
	}

	@Test
	public void testNullParameter() throws Exception {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new RecipeCommand()));
	}

	@Test
	void convert() {
		// given
		RecipeCommand source = new RecipeCommand();
		source.setId(RECIPE_ID);
		source.setDescription(LOREM_IPSUM);
		source.setDifficulty(Difficulty.MODERATE);
		source.setCookTime(TIME_MIN);
		source.setPrepTime(TIME_MIN);
		source.setServings(SERVINGS);
		source.setDirections(HOCUS_POCUS);
		source.setSource(HOTUM_FACTOTUM);
		source.setUrl(EXAMPLE_URL);
		
		NotesCommand notes = new NotesCommand();
		notes.setId(NOTES_ID);
		notes.setRecipeNotes(LOREM_IPSUM);
		
		source.setNotes(notes);
		
		UnitOfMeasureCommand uom1 = new UnitOfMeasureCommand();
		uom1.setId(UOM_ID_1);
		uom1.setDescription(UOM_1);
		UnitOfMeasureCommand uom2 = new UnitOfMeasureCommand();
		uom2.setId(UOM_ID_2);
		uom2.setDescription(UOM_2);
		
		CategoryCommand cat = new CategoryCommand();
		cat.setId(CAT_ID);
		cat.setDescription(CAT);
		
		Set<CategoryCommand> categories = new HashSet<>();
		categories.add(cat);
		source.setCategories(categories);
		
		IngredientCommand ing1 = new IngredientCommand();
		ing1.setId(ING_ID_1);
		ing1.setAmount(new BigDecimal(1));
		ing1.setUom(uom1);
		ing1.setDescription(ING_1);

		IngredientCommand ing2 = new IngredientCommand();
		ing2.setId(ING_ID_2);
		ing2.setAmount(new BigDecimal(8));
		ing2.setUom(uom2);
		ing2.setDescription(ING_2);

		IngredientCommand ing3 = new IngredientCommand();
		ing3.setId(ING_ID_3);
		ing3.setAmount(new BigDecimal(4));
		ing3.setUom(uom2);
		ing3.setDescription(ING_3);

		Set<IngredientCommand> ingredients = new HashSet<>();
		ingredients.add(ing1);
		ingredients.add(ing2);
		ingredients.add(ing3);
		source.setIngredients(ingredients);

		// when
		Recipe dest = converter.convert(source);
		
		Map<String, Identifiable> catMap = toMap(dest.getCategories());
		Map<String, Identifiable> ingMap = toMap(dest.getIngredients());
		
		
		// then
		assertNotNull(dest);
		assertNotNull(dest.getNotes());
		assertNotNull(dest.getCategories());
		assertNotNull(dest.getIngredients());
		assertEquals(source.getCategories().size(), dest.getCategories().size());
		assertEquals(source.getIngredients().size(), dest.getIngredients().size());
		
		assertEquals(RECIPE_ID, dest.getId());
		assertEquals(LOREM_IPSUM, dest.getDescription());
		assertEquals(Difficulty.MODERATE, dest.getDifficulty());
		assertEquals(TIME_MIN, dest.getPrepTime());
		assertEquals(TIME_MIN, dest.getCookTime());
		assertEquals(SERVINGS, dest.getServings());
		assertEquals(HOCUS_POCUS, dest.getDirections());
		assertEquals(HOTUM_FACTOTUM, dest.getSource());
		assertEquals(EXAMPLE_URL, dest.getUrl());
		
		Notes destNotes = dest.getNotes();
		assertEquals(NOTES_ID, destNotes.getId());
		assertEquals(LOREM_IPSUM, destNotes.getRecipeNotes());
		
		for (CategoryCommand s : source.getCategories()) {
			assumeTrue(catMap.containsKey(s.getId()));
			Category d = (Category) catMap.get(s.getId());
			assertEquals(s.getId(), d.getId());
			assertEquals(s.getDescription(), d.getDescription());
		}
		
		for (IngredientCommand s : source.getIngredients()) {
			assumeTrue(ingMap.containsKey(s.getId()));
			Ingredient d = (Ingredient) ingMap.get(s.getId());
			assertNotNull(d.getUom());
			assertEquals(s.getId(), d.getId());
			assertEquals(s.getAmount(), d.getAmount());
			assertEquals(s.getDescription(), d.getDescription());
			assertEquals(s.getUom().getId(), d.getUom().getId());
			assertEquals(s.getUom().getDescription(), d.getUom().getDescription());
		}
	}

	private Map<String, Identifiable> toMap(Set<? extends Identifiable> identifiables) {
		Map<String, Identifiable> retval = new HashMap<>();
		identifiables.forEach(record -> retval.put(record.getId(), record));
		return retval;
		
	}
}
