package pl.flez.spring.reactive.handlers;

import java.lang.reflect.Field;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import pl.flez.spring.reactive.data.User;
import pl.flez.spring.reactive.repositories.UserRepository;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

	private final UserRepository repository;

	public Mono<ServerResponse> findPageable(ServerRequest request) {
		if(request.queryParam("page").isPresent() && request.queryParam("size").isPresent()) {
			final PageRequest pageable = PageRequest.of(Integer.valueOf(request.queryParam("page").get()), Integer.valueOf(request.queryParam("size").get()));
			Document predicate = createDocumentFromParams(request);
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(repository.findPredicatePageable(predicate, pageable), User.class);
		} 		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(repository.findAll(), User.class);
	}
	
	public Mono<ServerResponse> save(ServerRequest request) {
		Mono<User> body = request.bodyToMono(User.class);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMap(repository::save),	User.class);
	}

	public Mono<ServerResponse> findById(ServerRequest request) {
		ObjectId id = new ObjectId((request.pathVariable("id")));		
		return repository.findById(id).flatMap(
				obj -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(obj)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> deleteById(ServerRequest request) {
		ObjectId id = new ObjectId((request.pathVariable("id")));		
		return repository.deleteById(id).flatMap(obj -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(obj)));
	}
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(repository.findAll(), User.class);
	}

	public Mono<ServerResponse> findAllExample(ServerRequest request) {
		Mono<User> body = request.bodyToMono(User.class);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMapMany(b-> repository.findAll(Example.of(b))), User.class);
	}
	
	public Mono<ServerResponse> findOneExample(ServerRequest request) {
		Mono<User> body = request.bodyToMono(User.class);		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMap(b->repository.findOne(Example.of(b))), User.class);
	}
	
	public Document createDocumentFromParams(ServerRequest request) {		
		Document doc = new Document();
		MultiValueMap<String, String> params = request.queryParams();
		for(Field f : User.class.getDeclaredFields()) {
			if(params.containsKey(f.getName())) {				
				doc.put(f.getName(), params.getFirst(f.getName()));
			}
		}
		return doc;
	}
	
}
