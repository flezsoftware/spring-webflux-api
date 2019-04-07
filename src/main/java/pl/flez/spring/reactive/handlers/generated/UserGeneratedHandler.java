package pl.flez.spring.reactive.handlers.generated;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import pl.flez.spring.reactive.data.User;
import pl.flez.spring.reactive.handlers.canon.SpringMongoWebFluxAutoHandler;
import pl.flez.spring.reactive.repositories.UserRepository;
@Component
public class UserGeneratedHandler extends SpringMongoWebFluxAutoHandler<User> {
	public UserGeneratedHandler(UserRepository repository) {
		super(repository,User.class);
	}
}
