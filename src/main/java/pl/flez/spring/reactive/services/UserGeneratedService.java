package pl.flez.spring.reactive.services;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import pl.flez.spring.reactive.data.User;
import pl.flez.spring.reactive.repositories.UserRepository;
import pl.flez.spring.reactive.services.canon.SpringMongoWebFluxAutoService;
import pl.flez.spring.reactive.utils.PageAndSortResolver;

@Service
public class UserGeneratedService extends SpringMongoWebFluxAutoService<User, ObjectId> {

	public UserGeneratedService(UserRepository repository, ReactiveMongoTemplate template) {
		super(repository,template,User.class);
	}
}
