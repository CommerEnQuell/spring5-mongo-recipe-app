package guru.springframework.recipe.commands;

import java.math.BigDecimal;

import guru.springframework.recipe.domain.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand implements Identifiable {

	private String id;
	private String recipeId;
	private BigDecimal amount;
	private String description;
	private UnitOfMeasureCommand uom;
}
