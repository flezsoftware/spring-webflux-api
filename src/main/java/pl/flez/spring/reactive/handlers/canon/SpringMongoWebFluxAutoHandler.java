package pl.flez.spring.reactive.handlers.canon;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public abstract class SpringMongoWebFluxAutoHandler<T,ID> {

	private final ReactiveMongoRepository<T, ID> repository;
	
	private final Class<T> reference;
	
	//private final ParameterizedTypeReference<T> reference = new ParameterizedTypeReference<T>() {}; // - REST ok , WebClient returns LinkedHashMap
	
	public SpringMongoWebFluxAutoHandler(ReactiveMongoRepository<T, ID> repository, Class<T> reference) {
		this.repository = repository;
		this.reference = reference;
	}

	public Mono<ServerResponse> save(ServerRequest request) {
		Mono<T> body = request.bodyToMono(reference);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMap(repository::save),	reference);
	}

	public Mono<ServerResponse> findById(ServerRequest request) {
		ID id = (ID)request.pathVariable("id");	
		return repository.findById(id).flatMap(
				obj -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(obj)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> deleteById(ServerRequest request) {
		ID id = (ID)request.pathVariable("id");		
		return repository.deleteById(id).flatMap(obj -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(obj)));
	}
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(repository.findAll(), reference);
	}

	public Mono<ServerResponse> findAllExample(ServerRequest request) {
		Mono<T> body = request.bodyToMono(reference);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMapMany(b-> repository.findAll(Example.of(b))), reference);
	}
	
	public Mono<ServerResponse> findOneExample(ServerRequest request) {
		Mono<T> body = request.bodyToMono(reference);		
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(body.flatMap(b->repository.findOne(Example.of(b))), reference);
	}
	
}
