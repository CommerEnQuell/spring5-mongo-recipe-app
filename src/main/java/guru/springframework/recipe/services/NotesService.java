package guru.springframework.recipe.services;

import java.util.Optional;

import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.domain.Notes;

public interface NotesService {

	public Optional<Notes> findById(String id);
	
	public Notes save(Notes notes);
	
	public NotesCommand saveNotesCommand(NotesCommand command);
}
