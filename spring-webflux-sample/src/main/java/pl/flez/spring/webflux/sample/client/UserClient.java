package pl.flez.spring.webflux.sample.client;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import pl.flez.spring.webflux.core.client.SpringMongoWebFluxAutoWebClient;
import pl.flez.spring.webflux.sample.data.User;
//@Component
public class UserClient extends SpringMongoWebFluxAutoWebClient<User,ObjectId> {
	public UserClient(String host, String path) {
		super(host, path, User.class);
	}
}
