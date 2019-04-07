package pl.flez.spring.reactive.client;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import pl.flez.spring.reactive.data.User;
import pl.flez.spring.reactive.routers.UserRouter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class UserWebClient {

	public final static String path = "/user";
	public final static String pathOne = path + "/{0}";
	public final static String pathExample = path + "/find";
	public final static String pathOneExample = path + "/find-one";
	
	private WebClient client = WebClient.create("http://localhost:8080");


	public void testClient() {
		User object = new User();
		object.setName("Imie");
		object.setSurname("Nazwisko");
		object.setEmail("email@email.com");		
		
		System.out.println(object.getId());
		
		object = post(path,object);
		System.out.println("save " + object.getId());
		
		
		object = getById(pathOne,object.getId());
		System.out.println("findById " + object.getId());
		
		object = post(pathOneExample, object);
		System.out.println("findOne " + object.getId());
		
		Flux<User> all = getAll(path);
		List<User> lst =  all.toStream().collect(Collectors.toList());
		System.out.println("findAll() size " + lst.size());
		
		delete(pathOne,object.getId());
		System.out.println("delete " + object.getId());
		
		object.setId(null);
		all = getAllExample(object, pathExample);
		lst =  all.toStream().collect(Collectors.toList());
		System.out.println("findAll(Example<s>) size " + lst.size());		
		
	
		
		all = getAll(path);
		lst =  all.toStream().collect(Collectors.toList());
		System.out.println("findAll() size " + lst.size());
		
	}

	public User post(String path,User object) {	
		Mono<ClientResponse> result = client.post().uri(path).body(BodyInserters.fromObject(object)).exchange();	
		return result.flatMap(res->res.bodyToMono(User.class)).block();				
	}
	
	public Flux<User> getAll(String path) {
		 return client.get().uri(path).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(User.class);
	}
	
	public Flux<User> getAllExample(User object,String path) {
		 return client.post().uri(path).body(BodyInserters.fromObject(object)).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(User.class);
	}	
	
	public User getById(String path,ObjectId id) {
		 String url =  MessageFormat.format(path, new Object[] {id.toString()});
		 Mono<ClientResponse> result = client.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
		 return result.flatMap(res->res.bodyToMono(User.class)).block();	
	}
	
	public Void delete(String path,ObjectId id) {
		 String url =  MessageFormat.format(path, new Object[] {id.toString()});
		 Mono<ClientResponse> result = client.delete().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
		 return result.flatMap(res->res.bodyToMono(Void.class)).block();	
	}
}
