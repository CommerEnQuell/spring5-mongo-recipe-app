package guru.springframework.recipe.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.UnitOfMeasure;

class IngredientToIngredientCommandTest {

	private static final String LONG_VALUE = "1";
	private static final String UOM_ID = "2";
	private static final BigDecimal AMOUNT_ONE = new BigDecimal(1);
	private static final String LOREM_IPSUM = "Lorem ipsum etc.";
	
	IngredientToIngredientCommand converter;

	@Before
	void setUp() throws Exception {
		converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
	}

	@Test
	public void testNullParameter() throws Exception {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new Ingredient()));
	}
	
	@Test
	public void convert() throws Exception {
		// given
		Ingredient source = new Ingredient();
		source.setId(LONG_VALUE);
		source.setAmount(AMOUNT_ONE);
		source.setDescription(LOREM_IPSUM);
		
		UnitOfMeasure sourceUom = new UnitOfMeasure();
		sourceUom.setId(UOM_ID);
		sourceUom.setDescription(LOREM_IPSUM);
		source.setUom(sourceUom);
		
		// when
		IngredientCommand dest = converter.convert(source);
		
		// then
		assertNotNull(dest);
		assertNotNull(dest.getUom());
		assertEquals(LONG_VALUE, dest.getId());
		assertEquals(AMOUNT_ONE, dest.getAmount());
		assertEquals(LOREM_IPSUM, dest.getDescription());
		assertEquals(UOM_ID, dest.getUom().getId());
		assertEquals(LOREM_IPSUM, dest.getUom().getDescription());
	}
}
