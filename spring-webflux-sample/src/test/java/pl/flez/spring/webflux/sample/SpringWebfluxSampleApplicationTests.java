package pl.flez.spring.webflux.sample;

import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import pl.flez.spring.webflux.sample.client.UserClient;
import pl.flez.spring.webflux.sample.data.User;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringWebfluxSampleApplicationTests {


	@LocalServerPort // to inject port value
	int port;
	
	@Autowired
	private WebTestClient webTestClient;
	public final static String path = "/user";
	public final static String pathOne = path + "/{0}";
	public final static String pathExample = path + "/find";
	public final static String pathOneExample = path + "/find-one";
	private final ParameterizedTypeReference<List<User>> reference = new ParameterizedTypeReference<List<User>>() {};

	@Test
	public void webTestClient() {
		User object = createMockObject();
		// POST
		User returnResult =  webTestClient.post().uri(path).body(BodyInserters.fromObject(object)).exchange()
		.expectStatus().isOk().expectBody(User.class).returnResult().getResponseBody();		
		assertEquals(object.getName(), returnResult.getName());
		
//		webTestClient.post().uri(path).body(BodyInserters.fromObject(object)).exchange()
//				.expectStatus().isOk().expectBody().jsonPath("$.id").isEqualTo(object.getName())
		
		// GET /{id}
		webTestClient.get().uri(MessageFormat.format(pathOne, new Object[]{returnResult.getId()})).accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk().expectBody(User.class).isEqualTo(returnResult);
		
		// GET /{id} 404
		webTestClient.get().uri(MessageFormat.format(pathOne, new Object[]{new ObjectId()})).accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isNotFound();
		
		// POST /find-one
		webTestClient.post().uri(pathOneExample).body(BodyInserters.fromObject(returnResult)).accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk().expectBody(User.class).isEqualTo(returnResult);
		
		// GET /		
		webTestClient.get().uri(path).accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk().expectBody(reference).isEqualTo(Collections.singletonList(returnResult));
		
		// GET /find
		webTestClient.post().uri(pathExample).body(BodyInserters.fromObject(object)).accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk().expectBody(reference).isEqualTo(Collections.singletonList(returnResult));
		
		// DELETE
		webTestClient.delete().uri(MessageFormat.format(pathOne, new Object[]{returnResult.getId()})).accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isOk().expectBody(Void.class);	
		
		// GET /{id} 404
		webTestClient.get().uri(MessageFormat.format(pathOne, new Object[]{returnResult.getId()})).accept(MediaType.APPLICATION_JSON).exchange()
		.expectStatus().isNotFound();		
	}


	@Test
	public void generatedTestClient() {
		UserClient client = new UserClient("http://localhost:" + port, "/user");
		User original = createMockObject();			
		User retrieved = createMockObject();		
		retrieved = client.save(original).block();
		assertEquals(original.getName(), retrieved.getName());
		assertEquals(retrieved.getId(),client.findById(retrieved.getId()).block().getId());	
		assertEquals(1,client.findAll().collectList().block().size());		
		assertEquals(retrieved.getId(),client.findOne(original).block().getId());		
		assertEquals(1,client.findAll(original).collectList().block().size());
		retrieved.setName("NewName");
		assertEquals(retrieved.getName(),client.update(retrieved.getId(),retrieved).block().getName());		
		client.delete(retrieved.getId()).block();
		assertEquals(0,client.findAll().collectList().block().size());		
	}
	
//	@Test 
//	public void addTest(){
//		UserClient client = new UserClient("http://localhost:" + port, "/user");
//		User original = createMockObject();	
//		
//		client.save(original).flatMap(r->{
//			assertEquals(r.getName(), original.getName());
//			return Mono.just(r);
//		}).flatMap(r->{
//			r.setName("NewName");
//			return client.update(r.getId(), r).flatMap(r1->{
//				assertEquals(r1.getName(), original.getName());
//				return Mono.just(r1);
//			}).checkpoint();			
//		}).checkpoint();
//		
//		client.save(original).flatMap(ret-> { 
//			assertEquals(original.getName(), ret.getName());
//			return Mono.just(ret);
//		}).block();
//	}
//	
	
	
	
	private static User createMockObject() {
		User object = new User();
		object.setName("Imie");
		object.setSurname("Nazwisko");
		object.setEmail("email@email.com");
		return object;
	}
	
}
