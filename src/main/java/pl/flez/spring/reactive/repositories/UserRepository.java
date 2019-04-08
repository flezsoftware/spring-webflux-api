package pl.flez.spring.reactive.repositories;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import pl.flez.spring.reactive.data.User;
import reactor.core.publisher.Flux;
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId>  {
	@Query("{}")
	 Flux<User> findPageable(Pageable pageable);	
	
	@Query("?0")
	 Flux<User> findPredicatePageable(Document predicate , Pageable pageable);
}