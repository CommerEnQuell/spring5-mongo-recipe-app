package guru.springframework.recipe.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
public class Notes implements Identifiable {
	
	private String id;
	private Recipe recipe;
	private String recipeNotes;
	
}
