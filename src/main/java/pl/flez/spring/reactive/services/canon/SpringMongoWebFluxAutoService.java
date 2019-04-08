package pl.flez.spring.reactive.services.canon;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;
import pl.flez.spring.reactive.services.CriteriaCreator;
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
		return template.find(CriteriaCreator.createQuery(parameters,clazz),clazz);
	}
	
	public Query createQuery(MultiValueMap<String, String> parameters) {
		
		Criteria criteria = Criteria.where("id").exists(true);
		
		parameters.forEach((String key, List<String> list)-> {
			
			list.stream().forEach(v -> System.out.println(v.getClass().getSimpleName()));
			
		});
		
		for(Field f : clazz.getDeclaredFields()) {
			if(parameters.containsKey(f.getName())) {
				
				final List<String> values = parameters.get(f.getName());
				for(String value : values) {
					criteria.and(f.getName()).regex(value, "i");
				}
				
//				parameters.forEach((String key, List<String> list)-> {
//					criteria = Criteria.where(f.getName()).regex(parameters.getFirst(f.getName()), "i");
//				});
				
				
				
				//Criteria.where("name").regex(parameters.getFirst("name").toString(), "i");
				
			}
		}
		
		return Query.query(criteria);
	}
}
