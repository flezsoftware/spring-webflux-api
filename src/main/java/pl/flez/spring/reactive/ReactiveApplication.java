package pl.flez.spring.reactive;

import java.time.LocalDate;
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
		
		
		for(int i =0; i < 0; i++)
		{
		User toFind = new User();
		toFind.setName("Im"+i);
		toFind.setSurname("Na"+i);
		toFind.setEmail("em"+i+"@email.com");;	
		toFind.setNumber(i);
		toFind.setNumberd(Double.valueOf(i));
		toFind.setDate(LocalDate.now().plusDays(i));
		toFind.setBool((i<5));
		
		User object = new User();
		object.setName("Im"+i);
		object.setSurname("Na"+i);
		object.setEmail("em"+i+"@email.com");		
		object.setNumber(i);
		object.setNumberd(Double.valueOf(i));
		object.setDate(LocalDate.now().plusDays(i));
		toFind.setBool((i<5));
		
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
			
		lst =  clientg.getAllExample(toFind).collectList().block();
		System.out.println("findAll(Example<s>) size " + lst.size());		
		
//		clientg.delete(object.getId());
//		System.out.println("delete " + object.getId());

		lst =  clientg.getAll().collectList().block();
		System.out.println("findAll() size " + lst.size());
		}
	}

}
