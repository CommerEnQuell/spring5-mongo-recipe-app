package guru.springframework.recipe.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public abstract class AbstractServiceImpl<T, ID> {
	
	protected final CrudRepository<T, ID> repository;
	
	protected AbstractServiceImpl(CrudRepository<T, ID> repository) {
		this.repository = repository;
	}
	
	public abstract Optional<T> findById(ID id);
	
	public Set<T> findAll() {
		Set<T> retval = new HashSet<>();
//		repository.findAll().forEach(t -> retval.add(t)); -- Replaced by alternative notation
		repository.findAll().iterator().forEachRemaining(retval::add);
		return retval;
	}

	public T save(T entity) {
		return repository.save(entity);
	}
	
	public void deleteById(ID id) {
		repository.deleteById(id);
	}
}
