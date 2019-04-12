package pl.flez.spring.webflux.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import pl.flez.spring.webflux.core.utils.PageAndSortResolver;
import pl.flez.spring.webflux.sample.client.UserClient;

@SpringBootApplication
public class SpringWebfluxSampleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxSampleApplication.class, args);
	}

	@Bean
	PageAndSortResolver pageAndSortResolver() {
		return new PageAndSortResolver();
	}	
}
