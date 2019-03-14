package guru.springframework.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.domain.Notes;
import lombok.Synchronized;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {
	
	@Synchronized
	@Nullable
	@Override
	public NotesCommand convert(Notes source) {
		if (source == null) {
			return null;
		}
		
		NotesCommand command = new NotesCommand();
		command.setId(source.getId());
		command.setRecipeNotes(source.getRecipeNotes());

		return command;
	}
}
