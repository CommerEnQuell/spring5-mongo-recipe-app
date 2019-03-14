package guru.springframework.recipe.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {
	
	Category category;
	
	@Before
	public void setUp() {
		category = new Category();
	}

	@Test
	public void getId() throws Exception {
		String idValue = "4";
		category.setId(idValue);
		assertEquals(idValue, category.getId());
	}

	@Test
	public void getDescription() throws Exception {
//		fail("Not yet implemented");
	}

	@Test
	public void getRecipes() throws Exception {
//		fail("Not yet implemented");
	}

}
