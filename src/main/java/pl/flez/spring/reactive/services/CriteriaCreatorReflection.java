package pl.flez.spring.reactive.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.MultiValueMap;
import org.springframework.util.NumberUtils;

public class CriteriaCreatorReflection {

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
		
	static public Criteria recognizeBooleanSyntax(String field, String value) {
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
	
	static public Criteria recognizeTextSyntax(String field, String value) {
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
	
	static public Criteria recognizeNumberSyntax(String field, String value, Class<? extends Number> numberClass) {		
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
			criteria = Criteria.where(field).is(NumberUtils.parseNumber(value, numberClass));
		} else if (ltMatcher.find()) {
			value = ltMatcher.group(1);
			criteria = Criteria.where(field).lt(NumberUtils.parseNumber(value, numberClass));
		} else if (lteMatcher.find()) {
			value = lteMatcher.group(1);
			criteria = Criteria.where(field).lte(NumberUtils.parseNumber(value, numberClass));
		} else if (gtMatcher.find()) {
			value = gtMatcher.group(1);
			criteria = Criteria.where(field).gt(NumberUtils.parseNumber(value, numberClass));
		} else if (gteMatcher.find()) {
			value = gteMatcher.group(1);
			criteria = Criteria.where(field).gte(NumberUtils.parseNumber(value, numberClass));
		}	else if (neMatcher.find()) {
			value = neMatcher.group(1);
			criteria = Criteria.where(field).ne(NumberUtils.parseNumber(value, numberClass));
		} else if (ninMatcher.find()) {
			value = ninMatcher.group(1);
			criteria = Criteria.where(field).nin(Arrays.asList(value.split(",")).stream().map(val -> NumberUtils.parseNumber(val, numberClass)).collect(Collectors.toList()));
		} else if (inMatcher.find()) {
			value = inMatcher.group(1);
			criteria = Criteria.where(field).in(Arrays.asList(value.split(",")).stream().map(val -> NumberUtils.parseNumber(val, numberClass)).collect(Collectors.toList()));
		} else if (existMatcher.find()) {
			value = existMatcher.group(1);
			criteria = Criteria.where(field).exists(Boolean.valueOf(value));
		} 
		
		return criteria;
	}
	

	
	public static Query createQuery2(MultiValueMap<String, String> parameters, Class<?> clazz) {
		final Criteria criteria = Criteria.where("id").exists(true);
		final List<Criteria> criteriasMain = new ArrayList<>();
		Field [] fields = clazz.getDeclaredFields();
		if(clazz.getSuperclass() != null) {
			System.out.println(clazz.getSuperclass().getSimpleName());
			fields =  Stream.concat(Arrays.stream(fields), Arrays.stream(clazz.getSuperclass().getDeclaredFields())).toArray(Field[]::new);
		}
		for (Field f : fields) {
			System.out.println(f.getName());
			if (parameters.containsKey(f.getName())) {	
				if(String.class.isAssignableFrom(f.getType())) {
					fillTextFieldMainCriterias(f,parameters,criteriasMain);
				} else
				if(Number.class.isAssignableFrom(f.getType())) {
					fillNumberFieldMainCriterias(f,parameters,criteriasMain);
				} else
				if(Boolean.class.isAssignableFrom(f.getType())) {
					fillBooleanFieldMainCriterias(f,parameters,criteriasMain);
				} else
				if(Object.class.isAssignableFrom(f.getType())) {
					System.out.println(f.getName());
				}	
			}
		}
		if (!criteriasMain.isEmpty()) {
			criteria.andOperator(criteriasMain.toArray((new Criteria[criteriasMain.size()])));
		}
		return Query.query(criteria);
	}
	
	
	public static void fillTextFieldMainCriterias(Field f,MultiValueMap<String, String> parameters,List<Criteria> criteriasMain) {
		final List<String> values = parameters.get(f.getName());				
		String fieldValue = parameters.getFirst(f.getName());		
		List<Criteria> fieldCriterias = createTextFieldCriterias(f,fieldValue, values);
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
	
	
	
	public static List<Criteria> createTextFieldCriterias(Field f,String fieldValue,List<String> values){
		List<Criteria> fieldCriterias = new ArrayList<>();
		Criteria fieldCriteria = recognizeTextSyntax(f.getName(),fieldValue);
		if(fieldCriteria!= null) {
			fieldCriterias.add(fieldCriteria);
		}
		if(values.size() > 1) {
			for(String additionalValue : values.subList(1, values.size())) {
				Criteria additionalCriteria = recognizeTextSyntax(f.getName(), additionalValue);
				if(additionalCriteria!= null) {
					fieldCriterias.add(additionalCriteria);
				}
			}					
		}
		return fieldCriterias;
	}
	
	public static void fillNumberFieldMainCriterias(Field f,MultiValueMap<String, String> parameters,List<Criteria> criteriasMain) {
		final List<String> values = parameters.get(f.getName());				
		String fieldValue = parameters.getFirst(f.getName());		
		List<Criteria> fieldCriterias = createNumberFieldCriterias(f,fieldValue, values);
		if(!fieldCriterias.isEmpty()) {
			System.out.println(fieldCriterias.size());
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
	
	public static List<Criteria> createNumberFieldCriterias(Field f,String fieldValue,List<String> values){
		List<Criteria> fieldCriterias = new ArrayList<>();
		Class<? extends Number> numberClass =  (Class<? extends Number>) f.getType();
		Criteria fieldCriteria = recognizeNumberSyntax(f.getName(),fieldValue,numberClass);
		if(fieldCriteria!= null) {
			fieldCriterias.add(fieldCriteria);
		}
		if(values.size() > 1) {
			for(String additionalValue : values.subList(1, values.size())) {
				Criteria additionalCriteria = recognizeNumberSyntax(f.getName(), additionalValue, numberClass);
				if(additionalCriteria!= null) {
					fieldCriterias.add(additionalCriteria);
				}
			}					
		}
		return fieldCriterias;
	}
	
	public static void fillBooleanFieldMainCriterias(Field f,MultiValueMap<String, String> parameters,List<Criteria> criteriasMain) {
		final List<String> values = parameters.get(f.getName());				
		String fieldValue = parameters.getFirst(f.getName());		
		List<Criteria> fieldCriterias = createBooleanFieldCriterias(f,fieldValue, values);
		if(!fieldCriterias.isEmpty()) {
			System.out.println(fieldCriterias.size());
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
	
	public static List<Criteria> createBooleanFieldCriterias(Field f,String fieldValue,List<String> values){
		List<Criteria> fieldCriterias = new ArrayList<>();
		Criteria fieldCriteria = recognizeBooleanSyntax(f.getName(),fieldValue);
		if(fieldCriteria!= null) {
			fieldCriterias.add(fieldCriteria);
		}
		if(values.size() > 1) {
			for(String additionalValue : values.subList(1, values.size())) {
				Criteria additionalCriteria = recognizeBooleanSyntax(f.getName(), additionalValue);
				if(additionalCriteria!= null) {
					fieldCriterias.add(additionalCriteria);
				}
			}					
		}
		return fieldCriterias;
	}

}
