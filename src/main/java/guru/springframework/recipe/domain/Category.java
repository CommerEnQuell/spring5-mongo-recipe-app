package guru.springframework.recipe.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category implements Identifiable {

	private String id;
	private String description;
	private Set<Recipe> recipes = new HashSet<>();
	
}
