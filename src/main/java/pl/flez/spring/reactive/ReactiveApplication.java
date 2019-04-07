package pl.flez.spring.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import pl.flez.spring.reactive.client.UserWebClient;
import pl.flez.spring.reactive.client.generated.UserGeneratedWebClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableMongoAuditing
public class ReactiveApplication {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		SpringApplication.run(ReactiveApplication.class, args);
		UserWebClient client = new UserWebClient();
		client.testClient();
		
		UserGeneratedWebClient clientg = new UserGeneratedWebClient("http://localhost:8080", "/user");
		clientg.testClient();
	}

}
