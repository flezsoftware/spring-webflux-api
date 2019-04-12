package pl.flez.spring.webflux.sample.services;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import pl.flez.spring.webflux.core.services.SpringMongoWebFluxAutoService;
import pl.flez.spring.webflux.sample.data.User;
import pl.flez.spring.webflux.sample.repositories.UserRepository;

@Service
public class UserService extends SpringMongoWebFluxAutoService<User, ObjectId> {

	public UserService(UserRepository repository, ReactiveMongoTemplate template) {
		super(repository, template, User.class);
	}
}
