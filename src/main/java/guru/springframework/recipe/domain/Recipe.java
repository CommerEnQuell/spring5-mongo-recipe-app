package guru.springframework.recipe.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipe implements Identifiable {

	private String id;
	private String description;
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	private String directions;
	private Set<Ingredient> ingredients = new HashSet<>();
	private Byte[] image;
	private Difficulty difficulty;
	private Notes notes;
	private Set<Category> categories = new HashSet<>();

	public Recipe addIngredient(Ingredient ingredient) {
		ingredients.add(ingredient);
		ingredient.setRecipe(this);
		return this;
	}
	
	public void setNotes(Notes notes) {
		if (notes != null) {
			this.notes = notes;
			notes.setRecipe(this);
		}
	}
	
}
