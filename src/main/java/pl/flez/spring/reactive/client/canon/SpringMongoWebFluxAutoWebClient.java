package pl.flez.spring.reactive.client.canon;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class SpringMongoWebFluxAutoWebClient<T> {
	private final String path;
	private final String pathOne;
	private final String pathExample;
	private final String pathOneExample;	
	private final WebClient client;
	private final Class<T> clazz;
	
	public SpringMongoWebFluxAutoWebClient(String host, String path, Class<T> clazz) {
		this.client = WebClient.create(host);		
		this.path = path;
		this.pathOne = path + "/{id}";
		this.pathExample = path + "/find";
		this.pathOneExample = path + "/find-one";
		this.clazz = clazz;
	}

	public void testClient() throws InstantiationException, IllegalAccessException {
		T object =  clazz.newInstance();	
		object = post(path,object);		

		
		object = post(pathOneExample, object);
		
		
		Flux<T> all = getAll(path);
		List<T> lst =  all.toStream().collect(Collectors.toList());
		System.out.println("findAll() size " + lst.size());
		

		all = getAllExample(object, pathExample);
		lst =  all.toStream().collect(Collectors.toList());
		System.out.println("findAll(Example<s>) size " + lst.size());		
		
	
		
		all = getAll(path);
		lst =  all.toStream().collect(Collectors.toList());
		System.out.println("findAll() size " + lst.size());
		
	}

	public T post(String path,T object) {	
		Mono<ClientResponse> result = client.post().uri(path).body(BodyInserters.fromObject(object)).exchange();	
		return result.flatMap(res->res.bodyToMono(clazz)).block();				
	}
	
	public Flux<T> getAll(String path) {
		 return client.get().uri(path).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(clazz);
	}
	
	public Flux<T> getAllExample(T object,String path) {
		 return client.post().uri(path).body(BodyInserters.fromObject(object)).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(clazz);
	}	
	
	public T getById(String path,ObjectId id) {
		 String url =  MessageFormat.format(path, new Object[] {id.toString()});
		 Mono<ClientResponse> result = client.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
		 return result.flatMap(res->res.bodyToMono(clazz)).block();	
	}
	
	public Void delete(String path,ObjectId id) {
		 String url =  MessageFormat.format(path, new Object[] {id.toString()});
		 Mono<ClientResponse> result = client.delete().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
		 return result.flatMap(res->res.bodyToMono(Void.class)).block();	
	}
}
