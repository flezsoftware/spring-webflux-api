package pl.flez.spring.reactive.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import pl.flez.spring.reactive.data.User;
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId>  {
	
}
