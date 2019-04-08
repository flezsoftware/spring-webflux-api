package pl.flez.spring.reactive.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import pl.flez.spring.reactive.handlers.UserHandler;

//@Configuration
public class UserRouter {
//	@Bean
//	public RouterFunction<ServerResponse> routeUser(UserHandler hander) {
//		return RouterFunctions
//			.route(RequestPredicates.GET("/user").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hander::getUser);
//	}
	public final static String path = "/user";
	public final static String pathOne = path + "/{id}";
	public final static String pathExample = path + "/find";
	public final static String pathOneExample = path + "/find-one";
	
	@Bean
	public RouterFunction<ServerResponse> routeUserController(UserHandler handler) {
		return RouterFunctions
			.route()
		    .GET(pathOne, RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::findById) 
		    .GET(path, RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::findPageable) 
		    
		    .POST(path, handler::save) 	
		    .DELETE(pathOne, handler::deleteById)
		    
		    .POST(pathExample, handler::findAllExample)
		    .POST(pathOneExample, handler::findOneExample)
		    //.add(otherRoute) 
		    .build();
	}
	
}
