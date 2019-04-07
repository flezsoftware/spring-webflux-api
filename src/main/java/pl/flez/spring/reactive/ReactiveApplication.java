package pl.flez.spring.reactive;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import pl.flez.spring.reactive.client.UserWebClient;
import pl.flez.spring.reactive.client.generated.UserGeneratedWebClient;
import pl.flez.spring.reactive.data.User;
import reactor.core.publisher.Flux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableMongoAuditing
public class ReactiveApplication {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		SpringApplication.run(ReactiveApplication.class, args);
//		UserWebClient client = new UserWebClient();
//		client.testClient();
		
		UserGeneratedWebClient clientg = new UserGeneratedWebClient("http://localhost:8080", "/user");
		User object = new User();
		object.setName("Imie");
		object.setSurname("Nazwisko");
		object.setEmail("email@email.com");		
		
		System.out.println(object.getId());
		
		object = clientg.post(object);
		System.out.println("save " + object.getId());
		
		
		object = clientg.getById(object.getId());
		System.out.println("findById " + object.getId());
		
		object = clientg.post(object);
		System.out.println("findOne " + object.getId());
		
		List<User> lst =  clientg.getAll().collectList().block();
		System.out.println("findAll() size " + lst.size());
		
		object = clientg.findOneExample(object);
		System.out.println("findOne(Example<s>) " + object.getId());
		
		object.setId(null);
		object.setCreatedDate(null);
		object.setUpdatedDate(null);
		
		
		lst =  clientg.getAllExample(object).collectList().block();
		System.out.println("findAll(Example<s>) size " + lst.size());		
		
		clientg.delete(object.getId());
		System.out.println("delete " + object.getId());

		lst =  clientg.getAll().collectList().block();
		System.out.println("findAll() size " + lst.size());
	}

}
