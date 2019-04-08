package pl.flez.spring.reactive.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.MultiValueMap;

public class CriteriaCreator {

	private static final String bracket = "\\((.*?)\\)";
	private static final String cnt = "cnt";
	private static final String cnti = "cnti";
	private static final String eq = "eq";
	private static final String lt = "lt";
	private static final String lte = "lte";
	private static final String gt = "gt";
	private static final String gte = "gte";
	
	private static final Pattern CNT = Pattern.compile(cnt + bracket);
	private static final Pattern CNTI = Pattern.compile(cnti + bracket);
	private static final Pattern EQ = Pattern.compile(eq + bracket);
	private static final Pattern LT = Pattern.compile(lt + bracket);
	private static final Pattern LTE = Pattern.compile(lte + bracket);
	private static final Pattern GT = Pattern.compile(gt + bracket);
	private static final Pattern GTE = Pattern.compile(gte + bracket);
	
	static public void recognizeFieldSyntax(Field field, String value, List<Criteria> criterias) {
		if (field.getType().isAssignableFrom(String.class)) {
			recognizeTextSyntax(field.getName(), value, criterias);
		} else if (field.getType().isAssignableFrom(Integer.class)) {
			recognizeNumberSyntax(field.getName(), value, criterias);
		}
	}

	static public void recognizeTextSyntax(String field, String value, List<Criteria> criterias) {
		final Matcher cntMatcher = CNT.matcher(value);
		final Matcher cntiMatcher = CNTI.matcher(value);
		final Matcher eqMatcher = EQ.matcher(value);
		if (cntMatcher.find()) {
			value = cntMatcher.group(1);
			criterias.add(Criteria.where(field).regex(value));
		} else if (cntiMatcher.find()) {
			value = cntiMatcher.group(1);
			criterias.add(Criteria.where(field).regex(value, "i"));
		} else if (eqMatcher.find()) {
			value = eqMatcher.group(1);
			criterias.add(Criteria.where(field).is(value));
		}
	}

	static public void recognizeNumberSyntax(String field, String queryValue, List<Criteria> criterias) {
		final Matcher eqMatcher = EQ.matcher(queryValue);
		final Matcher ltMatcher = LT.matcher(queryValue);
		final Matcher lteMatcher = LTE.matcher(queryValue);
		final Matcher gtMatcher = GT.matcher(queryValue);
		final Matcher gteMatcher = GTE.matcher(queryValue);
		
		if (eqMatcher.find()) {
			queryValue = eqMatcher.group(1);
			criterias.add(Criteria.where(field).is(Integer.valueOf(queryValue)));
		} else if (ltMatcher.find()) {
			queryValue = ltMatcher.group(1);
			criterias.add(Criteria.where(field).lt(Integer.valueOf(queryValue)));
		} else if (lteMatcher.find()) {
			queryValue = lteMatcher.group(1);
			criterias.add(Criteria.where(field).lte(Integer.valueOf(queryValue)));
		} else if (gtMatcher.find()) {
			queryValue = gtMatcher.group(1);
			criterias.add(Criteria.where(field).gt(Integer.valueOf(queryValue)));
		} else if (gteMatcher.find()) {
			queryValue = gteMatcher.group(1);
			criterias.add(Criteria.where(field).gte(Integer.valueOf(queryValue)));
		}
	}
	
	public static Query createQuery(MultiValueMap<String, String> parameters, Class<?> clazz) {
		final Criteria criteria = Criteria.where("id").exists(true);
		final List<Criteria> criteriasMain = new ArrayList<>();
		for (Field f : clazz.getDeclaredFields()) {
			if (parameters.containsKey(f.getName())) {
				final List<String> values = parameters.get(f.getName());
				if (values.size() > 1) {
					final List<Criteria> criterias = new ArrayList<>();
					for (String value : values) {
						recognizeFieldSyntax(f, value, criterias);
					}
					if (!criterias.isEmpty()) {
						criteria.orOperator(criterias.toArray((new Criteria[criterias.size()])));
					}
				} else {
					String value = parameters.getFirst(f.getName());
					recognizeFieldSyntax(f, value, criteriasMain);
				}
			}
		}
		if (!criteriasMain.isEmpty()) {
			criteria.andOperator(criteriasMain.toArray((new Criteria[criteriasMain.size()])));
		}
		return Query.query(criteria);
	}
}
