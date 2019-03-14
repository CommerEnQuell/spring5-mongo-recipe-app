package guru.springframework.recipe.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.recipe.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

	Optional<Category> findByDescription(String description);
}
