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

public abstract class SpringMongoWebFluxAutoWebClient<T, ID> {
	private final String path;
	private final String pathOne;
	private final String pathExample;
	private final String pathOneExample;
	private final WebClient client;
	private final Class<T> clazz;

	public SpringMongoWebFluxAutoWebClient(String host, String path, Class<T> clazz) {
		this.client = WebClient.create(host);
		this.path = path;
		this.pathOne = path + "/{0}";
		this.pathExample = path + "/find";
		this.pathOneExample = path + "/find-one";
		this.clazz = clazz;
	}

	public Mono<T> post(T object) {
		Mono<ClientResponse> result = client.post().uri(path).body(BodyInserters.fromObject(object)).exchange();
		return result.flatMap(res -> res.bodyToMono(clazz));
	}

	public Flux<T> findAll() {
		return client.get().uri(path).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(clazz);
	}

	public Mono<T> findById(ID id) {
		final String url = MessageFormat.format(pathOne, new Object[] { id });
		final Mono<ClientResponse> result = client.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
		return result.flatMap(res -> res.bodyToMono(clazz));
	}

	public Mono<Void> delete(ID id) {
		final String url = MessageFormat.format(pathOne, new Object[] { id });
		final Mono<ClientResponse> result = client.delete().uri(url).accept(MediaType.APPLICATION_JSON).exchange();
		return result.flatMap(res -> res.bodyToMono(Void.class));
	}

	public Mono<T> findOne(T object) {
		Mono<ClientResponse> result = client.post().uri(pathOneExample).body(BodyInserters.fromObject(object))
				.exchange();
		return result.flatMap(res -> res.bodyToMono(clazz));
	}

	public Flux<T> findAll(T object) {
		return client.post().uri(pathExample).body(BodyInserters.fromObject(object)).accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToFlux(clazz);
	}
}
