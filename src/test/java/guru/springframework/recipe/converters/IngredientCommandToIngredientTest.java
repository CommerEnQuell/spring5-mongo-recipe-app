package guru.springframework.recipe.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.domain.Ingredient;

class IngredientCommandToIngredientTest {

	private static final String LONG_VALUE = "1";
	private static final String UOM_ID = "2";
	private static final BigDecimal AMOUNT_ONE = new BigDecimal(1);
	private static final String LOREM_IPSUM = "Lorem ipsum etc.";
	
	IngredientCommandToIngredient converter;

	@Before
	void setUp() throws Exception {
		converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
	}

	@Test
	public void testNullParameter() throws Exception {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new IngredientCommand()));
	}
	
	@Test
	public void convert() throws Exception {
		// given
		IngredientCommand source = new IngredientCommand();
		source.setId(LONG_VALUE);
		source.setAmount(AMOUNT_ONE);
		source.setDescription(LOREM_IPSUM);
		
		UnitOfMeasureCommand sourceUom = new UnitOfMeasureCommand();
		sourceUom.setId(UOM_ID);
		sourceUom.setDescription(LOREM_IPSUM);
		source.setUom(sourceUom);
		// when
		Ingredient dest = converter.convert(source);
		
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
