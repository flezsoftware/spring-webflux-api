package pl.flez.spring.reactive.data.subdata;

import lombok.Getter;
import lombok.Setter;
import pl.flez.spring.reactive.data.User;

@Getter
@Setter
public class Person {
	private String name;
	private String surname;
	private String email;
	private Other other;
}
