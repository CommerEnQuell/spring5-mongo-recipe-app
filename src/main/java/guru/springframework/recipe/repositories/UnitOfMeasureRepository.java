package guru.springframework.recipe.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.recipe.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

	Optional<UnitOfMeasure> findByDescription(String description);
}
