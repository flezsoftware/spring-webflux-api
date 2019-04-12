package pl.flez.spring.webflux.sample.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import pl.flez.spring.webflux.sample.data.User;
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId>  {
	
}