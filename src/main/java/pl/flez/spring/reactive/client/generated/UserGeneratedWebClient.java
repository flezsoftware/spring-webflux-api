package pl.flez.spring.reactive.client.generated;

import org.bson.types.ObjectId;

import pl.flez.spring.reactive.client.canon.SpringMongoWebFluxAutoWebClient;
import pl.flez.spring.reactive.data.User;

public class UserGeneratedWebClient extends SpringMongoWebFluxAutoWebClient<User,ObjectId> {
	public UserGeneratedWebClient(String host, String path) {
		super(host, path, User.class);
	}
}
