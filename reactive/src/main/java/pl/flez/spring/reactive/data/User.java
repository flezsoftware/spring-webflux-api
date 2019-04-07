package pl.flez.spring.reactive.data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class User extends Auditable {
	@Id
	private ObjectId id;	
	private String name;
	private String surname;
	private String email;	
}
