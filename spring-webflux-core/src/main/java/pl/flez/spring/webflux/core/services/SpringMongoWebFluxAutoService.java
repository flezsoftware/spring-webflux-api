package pl.flez.spring.webflux.core.services;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;
import pl.flez.spring.webflux.core.utils.CriteriaCreator;
import pl.flez.spring.webflux.core.utils.PageAndSortResolver;
import pl.flez.spring.webflux.core.utils.SpringCopyUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class SpringMongoWebFluxAutoService<T, ID> {

	private final ReactiveMongoRepository<T, ID> repository;

	private final ReactiveMongoTemplate template;

	private final Class<T> clazz;

	public Flux<T> findAll() {
		return repository.findAll();
	}

	public Mono<T> findById(ID id) {
		return repository.findById(id);
	}

	public Mono<T> save(T object) {
		return repository.save(object);
	}

	public Mono<T> update(ID id,T object) {
		return repository.findById(id).map(db->{ try {
			SpringCopyUtils.copyPropertiesNotNull(db, object);
		} catch (InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} return db; }).flatMap(repository::save);
	}
	
	public Mono<Void> deleteById(ID id) {
		return repository.deleteById(id);
	}

	public Flux<T> findAll(Example<T> example) {
		return repository.findAll(example);
	}

	public Mono<T> findOne(Example<T> example) {
		return repository.findOne(example);
	}

	public Flux<T> findAll(MultiValueMap<String, String> parameters) {
		return template.find(
				CriteriaCreator.createQuery(parameters, clazz).with(PageAndSortResolver.createPageable(parameters)),
				clazz);
	}

	public Flux<T> findAll(MultiValueMap<String, String> parameters, Pageable pageable) {
		return template.find(CriteriaCreator.createQuery(parameters, clazz).with(pageable), clazz);
	}

}
