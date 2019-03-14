package guru.springframework.recipe.repositories;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.recipe.domain.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
