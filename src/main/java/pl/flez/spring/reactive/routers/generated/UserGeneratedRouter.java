package pl.flez.spring.reactive.routers.generated;

import org.springframework.context.annotation.Configuration;

import pl.flez.spring.reactive.data.User;
import pl.flez.spring.reactive.handlers.generated.UserGeneratedHandler;
import pl.flez.spring.reactive.routers.canon.SpringMongoWebFluxAutoRouter;
@Configuration
public class UserGeneratedRouter extends SpringMongoWebFluxAutoRouter<User> {
	public UserGeneratedRouter(UserGeneratedHandler handler) {
		super(handler, "/user");
	}
}
