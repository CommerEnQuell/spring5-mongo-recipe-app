package guru.springframework.recipe.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl extends AbstractServiceImpl<Category, String> implements CategoryService {
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super(categoryRepository);
	}
	
	@Override
	public Optional<Category> findById(String id) {
		return repository.findById(id);
	}
	
	@Override
	public Set<Category> findAll() {
		return super.findAll();
	}

	@Override
	public Optional<Category> findByDescription(String description) {
		Optional<Category> retval = ((CategoryRepository) repository).findByDescription(description);
		if (retval == null || !retval.isPresent()) {
			throw new RuntimeException("Category not found: " + description);
		}
		
		return retval;
	}
	
	@Override
	public Category save(Category category) {
		Category retval = super.save(category);
		return retval;
	}

}
