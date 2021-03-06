package pl.flez.spring.webflux.core.controllers;

import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import pl.flez.spring.webflux.core.services.SpringMongoWebFluxAutoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public abstract class SpringMongoWebFluxAutoController<T, ID> {

	private final SpringMongoWebFluxAutoService<T, ID> service;

	@GetMapping
	public Flux<T> findAll(@RequestParam MultiValueMap<String, String> parameters) {
		return service.findAll(parameters);
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<T>> findById(@PathVariable("id") ID id){
		return service.findById(id)
                .map(object -> ResponseEntity.ok(object))
                .defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<T> save(@RequestBody T object) {
		return service.save(object);
	}
	
	@PatchMapping("/{id}")
	public Mono<T> update(@PathVariable("id") ID id,@RequestBody T object) {
		return service.update(id,object);
	}
	
	@DeleteMapping("/{id}")
	public Mono<Void> deleteById(@PathVariable("id") ID id){
		return service.deleteById(id);
	}
	
	@PostMapping("/find")
	public Flux<T> findAll(@RequestBody T object){
		return service.findAll(Example.of(object));
	}
	
	@PostMapping("/find-one")
	public Mono<T> findOne(@RequestBody T object){
		return service.findOne(Example.of(object));
	}
	
}
