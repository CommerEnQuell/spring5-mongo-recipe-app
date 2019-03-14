package guru.springframework.recipe.domain;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ingredient implements Identifiable {

	@Id
	private String id;
	private String description;
	private BigDecimal amount;
	
	@DBRef
	private UnitOfMeasure uom;
//	private Recipe recipe;
}
