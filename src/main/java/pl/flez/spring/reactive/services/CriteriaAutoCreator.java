package pl.flez.spring.reactive.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.MultiValueMap;

public class CriteriaAutoCreator  {
	private static final String bracket = "\\((.*?)\\)";
	private static final String cnt = "cnt";
	private static final String cnti = "cnti";
	private static final String ne = "ne";
	private static final String eq = "eq";
	private static final String lt = "lt";
	private static final String lte = "lte";
	private static final String gt = "gt";
	private static final String gte = "gte";
	private static final String in = "in";
	private static final String nin = "nin";
	private static final String exist = "exist";
	
	private static final Pattern CNT = Pattern.compile(cnt + bracket);
	private static final Pattern CNTI = Pattern.compile(cnti + bracket);
	private static final Pattern EQ = Pattern.compile(eq + bracket);
	private static final Pattern NE = Pattern.compile(ne + bracket);
	private static final Pattern LT = Pattern.compile(lt + bracket);
	private static final Pattern LTE = Pattern.compile(lte + bracket);
	private static final Pattern GT = Pattern.compile(gt + bracket);
	private static final Pattern GTE = Pattern.compile(gte + bracket);
	private static final Pattern IN = Pattern.compile(in + bracket);
	private static final Pattern NIN = Pattern.compile(nin + bracket);
	private static final Pattern EXIST = Pattern.compile(exist + bracket);
	
	private static final  RecognizeValueInterface text  = (field, value) -> { return recognizeText(field, value); };
	private static final RecognizeValueInterface number  = (field, value) -> { return recognizeNumber(field, value); };
	private static final RecognizeValueInterface bool  = (field, value) -> { return recognizeBoolean(field, value); };

	
	public static Query createQuery(MultiValueMap<String, String> parameters) {
		final Criteria criteria = Criteria.where("id").exists(true);
		final List<Criteria> criteriasMain = new ArrayList<>();		
		parameters.forEach((k,v) -> {
			String firstValue = parameters.getFirst(k);
			firstValue = StringUtils.substringBetween(firstValue, "(", ")");
			if (firstValue.equals("true") || firstValue.equals("false")) {
				createObjectCriteria(k, parameters, criteriasMain,bool);
			} else
			if(NumberUtils.isParsable(firstValue))	{
				createObjectCriteria(k, parameters, criteriasMain,number);
			} else			
			{
				createObjectCriteria(k, parameters, criteriasMain,text);
			}
		});
		if (!criteriasMain.isEmpty()) {
			criteria.andOperator(criteriasMain.toArray((new Criteria[criteriasMain.size()])));
		}
		return Query.query(criteria);
	}
	
	
	public static void createObjectCriteria(String fieldName,MultiValueMap<String, String> parameters,List<Criteria> criteriasMain, RecognizeValueInterface recoMethod) {
		final List<String> values = parameters.get(fieldName);				
		String fieldValue = parameters.getFirst(fieldName);				
		List<Criteria> fieldCriterias = createFieldCriteria(fieldName,fieldValue, values,recoMethod);
		if(!fieldCriterias.isEmpty()) {
			Criteria fieldCriteria = null;	
			if(fieldCriterias.size() > 1) {
				fieldCriteria = new Criteria();
				fieldCriteria.andOperator(fieldCriterias.toArray((new Criteria[fieldCriterias.size()])));
			}  else {
				fieldCriteria = fieldCriterias.get(0);
			}
			criteriasMain.add(fieldCriteria);
		}				
	}
	public static List<Criteria> createFieldCriteria(String fieldName,String fieldValue,List<String> values, RecognizeValueInterface method){
		List<Criteria> fieldCriterias = new ArrayList<>();
		Criteria fieldCriteria = method.recognizeValue(fieldName,fieldValue);
		if(fieldCriteria!= null) {
			fieldCriterias.add(fieldCriteria);
		}
		if(values.size() > 1) {
			for(String additionalValue : values.subList(1, values.size())) {
				Criteria additionalCriteria = method.recognizeValue(fieldName, additionalValue);
				if(additionalCriteria!= null) {
					fieldCriterias.add(additionalCriteria);
				}
			}					
		}
		return fieldCriterias;
	}
	
	
	public static Criteria recognizeBoolean(String field, String value) {
		Criteria criteria = null;
	
		final Matcher eqMatcher = EQ.matcher(value);
		final Matcher neMatcher = NE.matcher(value);
		final Matcher inMatcher = IN.matcher(value);
		final Matcher ninMatcher = NIN.matcher(value);
		final Matcher existMatcher = EXIST.matcher(value);
		
		if (eqMatcher.find()) {
			value = eqMatcher.group(1);
			criteria = Criteria.where(field).is(Boolean.valueOf(value));
		} else if (neMatcher.find()) {
			value = neMatcher.group(1);
			criteria = Criteria.where(field).ne((Boolean.valueOf(value)));
		} else if (ninMatcher.find()) {
			value = ninMatcher.group(1);
			criteria = Criteria.where(field).nin(Arrays.asList(value.split(",")).stream().map(val -> Boolean.valueOf(val)).collect(Collectors.toList()));
		} else if (inMatcher.find()) {
			value = inMatcher.group(1);
			criteria = Criteria.where(field).in(Arrays.asList(value.split(",")).stream().map(val -> Boolean.valueOf(val)).collect(Collectors.toList()));
		} else if (existMatcher.find()) {
			value = existMatcher.group(1);
			criteria = Criteria.where(field).exists(Boolean.valueOf(value));
		} 
		return criteria;
	}
	
	public static Criteria recognizeText(String field, String value) {
		Criteria criteria = null;
		final Matcher cntMatcher = CNT.matcher(value);
		final Matcher cntiMatcher = CNTI.matcher(value);
		final Matcher eqMatcher = EQ.matcher(value);
		final Matcher neMatcher = NE.matcher(value);
		final Matcher inMatcher = IN.matcher(value);
		final Matcher ninMatcher = NIN.matcher(value);
		final Matcher existMatcher = EXIST.matcher(value);
		
		if (cntMatcher.find()) {
			value = cntMatcher.group(1);
			criteria = Criteria.where(field).regex(value);
		} else if (cntiMatcher.find()) {
			value = cntiMatcher.group(1);
			criteria = Criteria.where(field).regex(value, "i");
		} else if (eqMatcher.find()) {
			value = eqMatcher.group(1);
			criteria = Criteria.where(field).is(value);
		} else if (neMatcher.find()) {
			value = neMatcher.group(1);
			criteria = Criteria.where(field).ne(value);
		} else if (ninMatcher.find()) {
			value = ninMatcher.group(1);
			criteria = Criteria.where(field).nin(Arrays.asList(value.split(",")));
		} else if (inMatcher.find()) {
			value = inMatcher.group(1);
			criteria = Criteria.where(field).in(Arrays.asList(value.split(",")));
		} else if (existMatcher.find()) {
			value = existMatcher.group(1);
			criteria = Criteria.where(field).exists(Boolean.valueOf(value));
		} 
		return criteria;
	}
	
	public static Criteria recognizeNumber(String field, String value) {		
		Criteria criteria = null;	
		final Matcher eqMatcher = EQ.matcher(value);
		final Matcher ltMatcher = LT.matcher(value);
		final Matcher lteMatcher = LTE.matcher(value);
		final Matcher gtMatcher = GT.matcher(value);
		final Matcher gteMatcher = GTE.matcher(value);
		final Matcher neMatcher = NE.matcher(value);
		final Matcher inMatcher = IN.matcher(value);
		final Matcher ninMatcher = NIN.matcher(value);		
		final Matcher existMatcher = EXIST.matcher(value);
		if (eqMatcher.find()) {
			value = eqMatcher.group(1);
			criteria = Criteria.where(field).is(NumberUtils.createNumber(value));
		} else if (ltMatcher.find()) {
			value = ltMatcher.group(1);
			criteria = Criteria.where(field).lt(NumberUtils.createNumber(value));
		} else if (lteMatcher.find()) {
			value = lteMatcher.group(1);
			criteria = Criteria.where(field).lte(NumberUtils.createNumber(value));
		} else if (gtMatcher.find()) {
			value = gtMatcher.group(1);
			criteria = Criteria.where(field).gt(NumberUtils.createNumber(value));
		} else if (gteMatcher.find()) {
			value = gteMatcher.group(1);
			criteria = Criteria.where(field).gte(NumberUtils.createNumber(value));
		}	else if (neMatcher.find()) {
			value = neMatcher.group(1);
			criteria = Criteria.where(field).ne(NumberUtils.createNumber(value));
		} else if (ninMatcher.find()) {
			value = ninMatcher.group(1);
			criteria = Criteria.where(field).nin(Arrays.asList(value.split(",")).stream().map(val -> NumberUtils.createNumber(val)).collect(Collectors.toList()));
		} else if (inMatcher.find()) {
			value = inMatcher.group(1);
			criteria = Criteria.where(field).in(Arrays.asList(value.split(",")).stream().map(val -> NumberUtils.createNumber(val)).collect(Collectors.toList()));
		} else if (existMatcher.find()) {
			value = existMatcher.group(1);
			criteria = Criteria.where(field).exists(Boolean.valueOf(value));
		} 
		
		return criteria;
	}
}
