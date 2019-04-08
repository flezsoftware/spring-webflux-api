package pl.flez.spring.reactive.routers.canon;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import pl.flez.spring.reactive.handlers.canon.SpringMongoWebFluxAutoHandler;

public abstract class SpringMongoWebFluxAutoRouter<T,ID> {
	private final String path;
	private final String pathOne;
	private final String pathExample;
	private final String pathOneExample;
	private final SpringMongoWebFluxAutoHandler<T,ID> handler;
	
	public SpringMongoWebFluxAutoRouter(SpringMongoWebFluxAutoHandler<T,ID> handler, String path) {
		this.handler = handler;
		this.path = path;
		this.pathOne = path + "/{id}";
		this.pathExample = path + "/find";
		this.pathOneExample = path + "/find-one";
	}
	@Bean
	public RouterFunction<ServerResponse> routeUserController() {
		return RouterFunctions
			.route()
		    .GET(pathOne, RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::findById) 
		    .GET(path, RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::findAll) 		    
		    .POST(path, handler::save) 	
		    .DELETE(pathOne, handler::deleteById)		    
		    .POST(pathExample, handler::findAllExample)
		    .POST(pathOneExample, handler::findOneExample)
		    .build();
	}
}
