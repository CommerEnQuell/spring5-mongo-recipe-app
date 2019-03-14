package guru.springframework.recipe.domain;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
public class Ingredient implements Identifiable {

	private String id;
	private String description;
	private BigDecimal amount;
	private UnitOfMeasure uom;
	private Recipe recipe;
}
