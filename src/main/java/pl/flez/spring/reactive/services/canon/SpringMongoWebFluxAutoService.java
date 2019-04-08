package pl.flez.spring.reactive.services.canon;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;
import pl.flez.spring.reactive.services.CriteriaAutoCreator;
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
	
	public Mono<T> findById(ID id){
		return repository.findById(id);
	}

	public Mono<T> save(T object) {
		return repository.save(object);
	}
	
	public Mono<Void> deleteById(ID id){
		return repository.deleteById(id);
	}
	
	public Flux<T> findAll(Example<T> example){
		return repository.findAll(example);
	}
	
	public Mono<T> findOne(Example<T> example){
		return repository.findOne(example);
	}
	
	public Flux<T> findAll(MultiValueMap<String, String> parameters){
		return template.find(CriteriaAutoCreator.createQuery(parameters),clazz);
	}
}
