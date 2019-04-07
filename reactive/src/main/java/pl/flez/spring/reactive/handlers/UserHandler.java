package pl.flez.spring.reactive.handlers;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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

	public Mono<ServerResponse> save(ServerRequest request) {
		Mono<User> body = request.bodyToMono(User.class);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMap(repository::save),
				User.class);
	}

	public Mono<ServerResponse> findById(ServerRequest request) {
		ObjectId id = new ObjectId(request.pathVariable("id"));
		return repository.findById(id).flatMap(
				obj -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(obj)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> deleteById(ServerRequest request) {
		ObjectId id = new ObjectId(request.pathVariable("id"));		
		return repository.deleteById(id).flatMap(
				obj -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(obj)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(repository.findAll(), User.class);
	}

	public Mono<ServerResponse> findAllExample(ServerRequest request) {
		Mono<User> body = request.bodyToMono(User.class);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMap(b->repository.findOne(Example.of(b))), User.class);
	}
	
	public Mono<ServerResponse> findOneExample(ServerRequest request) {
		Mono<User> body = request.bodyToMono(User.class);
		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMap(b->repository.findOne(Example.of(b))), User.class);
	}
	
	
}
