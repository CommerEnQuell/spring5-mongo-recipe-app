package guru.springframework.recipe.converters;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import lombok.Synchronized;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {
	private final NotesCommandToNotes notesConverter;
	private final IngredientCommandToIngredient ingredientConverter;
	private final CategoryCommandToCategory catConverter;

	public RecipeCommandToRecipe(CategoryCommandToCategory catConverter, IngredientCommandToIngredient ingredientConverter, NotesCommandToNotes notesConverter) {
		this.catConverter = catConverter;
		this.ingredientConverter = ingredientConverter;
		this.notesConverter = notesConverter;
	}
	
	@Synchronized
	@Nullable
	@Override
	public Recipe convert(RecipeCommand source) {
		if (source == null) {
			return null;
		}
		
		Recipe dest = new Recipe();
		dest.setId(source.getId());
		dest.setDescription(source.getDescription());
		dest.setDifficulty(source.getDifficulty());
		dest.setPrepTime(source.getPrepTime());
		dest.setCookTime(source.getCookTime());
		dest.setServings(source.getServings());
		dest.setSource(source.getSource());
		dest.setUrl(source.getUrl());
		dest.setDirections(source.getDirections());
		dest.setNotes(notesConverter.convert(source.getNotes()));
		if (source.getCategories() != null) {
			Set<Category> categories = new HashSet<>();
			source.getCategories().forEach(cat -> categories.add(catConverter.convert(cat)));
			dest.setCategories(categories);
		}
		if (source.getIngredients() != null) {
			Set<Ingredient> ingredients = new HashSet<>();
			source.getIngredients().forEach(ingredient -> ingredients.add(ingredientConverter.convert(ingredient)));
			dest.setIngredients(ingredients);
		}
		if (source.getImage() != null) {
			dest.setImage(source.getImage());
		}
		
		return dest;
	}
}
