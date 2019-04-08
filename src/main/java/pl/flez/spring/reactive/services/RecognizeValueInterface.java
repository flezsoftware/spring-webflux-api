package pl.flez.spring.reactive.services;

import org.springframework.data.mongodb.core.query.Criteria;

public interface RecognizeValueInterface {
	Criteria recognizeValue(String field, String value);	
}
