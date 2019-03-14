package guru.springframework.recipe.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitOfMeasure implements Identifiable {

	private String id;
	private String description;
}
