package pl.flez.spring.reactive.client.generated;

import pl.flez.spring.reactive.client.canon.SpringMongoWebFluxAutoWebClient;
import pl.flez.spring.reactive.data.User;

public class UserGeneratedWebClient extends SpringMongoWebFluxAutoWebClient<User> {
	public UserGeneratedWebClient(String host, String path) {
		super(host, path, User.class);
	}
}
