package guru.springframework.recipe.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Category implements Identifiable {
	
	@Id
	private String id;
	private String description;
	private Set<Recipe> recipes = new HashSet<>();
	
}
