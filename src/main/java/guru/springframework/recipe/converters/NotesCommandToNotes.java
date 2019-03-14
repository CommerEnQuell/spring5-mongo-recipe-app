package guru.springframework.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.domain.Notes;
import lombok.Synchronized;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {
	
	@Synchronized
	@Nullable
	@Override
	public Notes convert(NotesCommand source) {
		if (source == null) {
			return null;
		}
		
		Notes entity = new Notes();
		entity.setId(source.getId());
		entity.setRecipeNotes(source.getRecipeNotes());

		return entity;
	}
}
