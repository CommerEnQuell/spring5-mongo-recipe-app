package guru.springframework.recipe.repositories;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.recipe.domain.Notes;

public interface NotesRepository extends CrudRepository<Notes, String> {

}
