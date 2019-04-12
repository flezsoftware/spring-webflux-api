package pl.flez.spring.reactive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
@Configuration
@EnableWebFlux
public class WebFluxConfiguration 
implements WebFluxConfigurer  
{

	@Override
	public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {

		configurer.addCustomResolver();
	}
}
