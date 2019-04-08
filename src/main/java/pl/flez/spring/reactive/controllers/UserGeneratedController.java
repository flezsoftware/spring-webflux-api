package pl.flez.spring.reactive.controllers;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.flez.spring.reactive.controllers.canon.SpringMongoWebFluxAutoController;
import pl.flez.spring.reactive.data.User;
import pl.flez.spring.reactive.services.UserGeneratedService;
@RequestMapping("/user")
@RestController
public class UserGeneratedController extends SpringMongoWebFluxAutoController<User, ObjectId> {

	public UserGeneratedController(UserGeneratedService service) {
		super(service);
	}

}
