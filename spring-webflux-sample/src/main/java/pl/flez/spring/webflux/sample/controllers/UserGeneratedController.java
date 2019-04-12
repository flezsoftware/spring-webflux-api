package pl.flez.spring.webflux.sample.controllers;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.flez.spring.webflux.core.controllers.SpringMongoWebFluxAutoController;
import pl.flez.spring.webflux.sample.data.User;
import pl.flez.spring.webflux.sample.services.UserService;

@RequestMapping("/user")
@RestController
public class UserGeneratedController extends SpringMongoWebFluxAutoController<User, ObjectId> {

	public UserGeneratedController(UserService service) {
		super(service);
	}	
}
