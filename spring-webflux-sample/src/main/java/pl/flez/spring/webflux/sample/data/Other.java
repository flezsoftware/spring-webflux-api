package pl.flez.spring.webflux.sample.data;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Other {
	Integer field1;
	Boolean field2;
	Double field3;
	ObjectId otherId;
}
