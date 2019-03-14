package guru.springframework.recipe.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import guru.springframework.recipe.repositories.UnitOfMeasureRepository;

@Service
public class UnitOfMeasureServiceImpl extends AbstractServiceImpl<UnitOfMeasure, String> implements UnitOfMeasureService {
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
	
	public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		super(unitOfMeasureRepository);
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}
	
	@Override
	public Optional<UnitOfMeasure> findById(String id) {
		return repository.findById(id);
	}
	
	@Override
	public Set<UnitOfMeasure> findAll() {
		return super.findAll();
	}

	@Override
	public UnitOfMeasure findByDescription(String description) {
		UnitOfMeasureRepository uomRepository = (UnitOfMeasureRepository) repository;
		Optional<UnitOfMeasure> o = uomRepository.findByDescription(description);
		if (o == null || !o.isPresent()) {
			throw new RuntimeException("Unit of measure not found: " + description);
		}
		
		UnitOfMeasure retval = o.get();
		return retval;
	}
	
	@Override
	public UnitOfMeasure save(UnitOfMeasure unitOfMeasure) {
		UnitOfMeasure savedUOM = super.save(unitOfMeasure);
		return savedUOM;
	}
	
	@Override
	public Set<UnitOfMeasureCommand> listAllUoms() {
		return StreamSupport.stream(repository.findAll().spliterator(), false)
							.map(unitOfMeasureToUnitOfMeasureCommand::convert)
							.collect(Collectors.toSet());
	}
}
