package pl.flez.spring.webflux.sample.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
	private String name;
	private String surname;
	private String email;
	private Other other;
}
