package pl.flez.spring.reactive.utils;

import org.springframework.data.mongodb.core.query.Criteria;

public interface RecognizeValueInterface {
	Criteria recognizeValue(String field, String value);	
}
