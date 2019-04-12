package pl.flez.spring.reactive;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import pl.flez.spring.reactive.client.generated.UserGeneratedWebClient;
import pl.flez.spring.reactive.data.User;

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
		
		object = clientg.post(object).block();
		System.out.println("save " + object.getId());
		
		
		object = clientg.findById(object.getId()).block();
		System.out.println("findById " + object.getId());
		
		object = clientg.post(object).block();
		System.out.println("findOne " + object.getId());
		
		List<User> lst =  clientg.findAll().collectList().block();
		System.out.println("findAll() size " + lst.size());
		
		object = clientg.findOne(object).block();
		System.out.println("findOne(Example<s>) " + object.getId());
			
		lst =  clientg.findAll(toFind).collectList().block();
		System.out.println("findAll(Example<s>) size " + lst.size());		
		
//		clientg.delete(object.getId());
//		System.out.println("delete " + object.getId());

		lst =  clientg.findAll().collectList().block();
		System.out.println("findAll() size " + lst.size());
		}
	}

}
