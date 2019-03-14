package guru.springframework.recipe.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.domain.Notes;

class NotesCommandToNotesTest {
	private static final String LONG_VALUE = "1";
	private static final String LOREM_IPSUM = "Lorem ipsum etc.";
	
	NotesCommandToNotes converter;

	@Before
	void setUp() throws Exception {
		converter = new NotesCommandToNotes();
	}

	@Test
	public void testNullParameter() throws Exception {
		assertNull(converter.convert(null));
	}
	
	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new NotesCommand()));
	}
	
	@Test
	public void convert() throws Exception {
		// given
		NotesCommand source = new NotesCommand();
		source.setId(LONG_VALUE);
		source.setRecipeNotes(LOREM_IPSUM);
		
		// when
		Notes dest = converter.convert(source);
		
		// then
		assertNotNull(dest);
		assertEquals(LONG_VALUE, dest.getId());
		assertEquals(LOREM_IPSUM, dest.getRecipeNotes());
	}

}
