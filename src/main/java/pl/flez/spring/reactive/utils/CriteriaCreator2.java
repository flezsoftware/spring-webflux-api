package pl.flez.spring.reactive.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class CriteriaCreator2 {
	
	public static Query createQuery(MultiValueMap<String, String> parameters, Class<?> clazz) {
	
		final Criteria criteria = Criteria.where("id").exists(true);		
		List<Criteria> criterias = new ArrayList<Criteria>();
//		Class<?> varClass;
//		for(Entry<String, List<String>> entry : parameters.entrySet()) {			
//			if(entry.getKey().contains(".")) {
//				List<String> fieldNames = Arrays.asList(entry.getKey().split("\\."));
//				varClass = clazz;		
//				for(String fieldName : fieldNames) {				
//						varClass = varClass.getDeclaredField(fieldName).getType();					
//				}						
//				
//			}else {
//				  varClass  = clazz.getDeclaredField(entry.getKey()).getType();
//			}
//			criterias.add(createFieldcriteria(entry.getKey(), entry.getValue(),varClass));	
//		}
		
		parameters.forEach((k,v) ->{
			if(k.contains(fieldSeparator)) {
				String[] farr = k.split(splitFieldSeparator);
				List<String> fieldNames = Arrays.asList(farr);
				Class<?> varClass = clazz;		
				for(String fieldName : fieldNames) {
					try {
						Field field = varClass.getDeclaredField(fieldName);						
						varClass = returnFieldClass(field);
					} catch (NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					}
				}
				criterias.add(createFieldcriteria(k, v,varClass));			
				
			}else {
				try {
				  Class<?> varClass  = returnFieldClass(clazz.getDeclaredField(k));
				  criterias.add(createFieldcriteria(k, v,varClass));
				} catch (NoSuchFieldException | SecurityException e) {
				}
			}
		});
		if (!criterias.isEmpty()) {
			criteria.andOperator(criterias.toArray((new Criteria[criterias.size()])));
		}
		return Query.query(criteria);
	}
	
	static public Class<?> returnFieldClass(Field field){
		if(Collection.class.isAssignableFrom(field.getType())) {
			return ((Class<?>) ((ParameterizedType) field.getGenericType())
					.getActualTypeArguments()[0]);
			
		} else {
			return field.getType();
		}
	}
	
	public static Criteria createFieldcriteria(String k, List<String> v, Class<?> varClass) {
		  Criteria fieldCriteria = new Criteria();
		  List<Criteria> fieldCriterias = convertValues(k, v, varClass);
		  fieldCriteria.andOperator(fieldCriterias.toArray((new Criteria[fieldCriterias.size()])));
		  return fieldCriteria;
	}
	
	public static List<Criteria> convertValues(String field, List<String> v, Class<?> clazz){
		return  v.stream().map(value -> extractParamValue2(field,value, clazz)).collect(Collectors.toList());
	}
	
	public static Object convertFieldValue(String value, Class<?> clazz) {
		if(String.class.isAssignableFrom(clazz)){
			return value;
		} else if (Boolean.class.isAssignableFrom(clazz)) {
			return Boolean.valueOf(value);			
		} else if(Byte.class.isAssignableFrom(clazz)) {
			return Byte.valueOf(value);			
		} else if(Character.class.isAssignableFrom(clazz)) {
			return Character.valueOf(value.charAt(0));
		}  else if(Enum.class.isAssignableFrom(clazz)) {
			return value;
		}  else if (Integer.class.isAssignableFrom(clazz) && NumberUtils.isParsable(value)) {
			return Integer.valueOf(value);			
		} else if (Double.class.isAssignableFrom(clazz) && NumberUtils.isParsable(value)) {
			return Double.valueOf(value);			
		} else if (Float.class.isAssignableFrom(clazz) && NumberUtils.isParsable(value)) {
			return Float.valueOf(value);		
		} else if(Long.class.isAssignableFrom(clazz) && NumberUtils.isParsable(value)) {
			return Long.valueOf(value);			
		} else if(Short.class.isAssignableFrom(clazz) && NumberUtils.isParsable(value)) {
			return Short.valueOf(value);			
		} else if(BigDecimal.class.isAssignableFrom(clazz) && NumberUtils.isParsable(value)) {
			return NumberUtils.createNumber(value);			
		} else if(Number.class.isAssignableFrom(clazz) && NumberUtils.isParsable(value)) {
			return NumberUtils.createNumber(value);			
		} 
		else if(ObjectId.class.isAssignableFrom(clazz)) {
			return new ObjectId(value);
		}
		return value;
	}
	
	private static final String fieldSeparator = ".";
	private static final String splitFieldSeparator = "\\.";
	
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
	
	public static Criteria extractParamValue2(String field,String value, Class<?> clazz) {	
		final Matcher cntMatcher = CNT.matcher(value);
		final Matcher cntiMatcher = CNTI.matcher(value);
		final Matcher eqMatcher = EQ.matcher(value);
		final Matcher neMatcher = NE.matcher(value);
		final Matcher ltMatcher = LT.matcher(value);
		final Matcher lteMatcher = LTE.matcher(value);
		final Matcher gtMatcher = GT.matcher(value);
		final Matcher gteMatcher = GTE.matcher(value);		
		final Matcher inMatcher = IN.matcher(value);
		final Matcher ninMatcher = NIN.matcher(value);
		final Matcher existMatcher = EXIST.matcher(value);
		
		if (cntMatcher.find()) {
			value = cntMatcher.group(1);
			return Criteria.where(field).regex((String)convertFieldValue(value,clazz));
		} else if (cntiMatcher.find()) {
			value = cntiMatcher.group(1);
			return Criteria.where(field).regex((String)convertFieldValue(value,clazz), "i");
		} else if (eqMatcher.find()) {
			value = eqMatcher.group(1);
			return Criteria.where(field).is(convertFieldValue(value,clazz));
		} else if (neMatcher.find()) {
			value = neMatcher.group(1);
			return Criteria.where(field).ne(convertFieldValue(value,clazz));
		} else if (ltMatcher.find()) {
			value = ltMatcher.group(1);
			return Criteria.where(field).lt(convertFieldValue(value,clazz));
		} else if (lteMatcher.find()) {
			value = lteMatcher.group(1);
			return Criteria.where(field).lte(convertFieldValue(value,clazz));
		} else if (gtMatcher.find()) {
			value = gtMatcher.group(1);
			return Criteria.where(field).gt(convertFieldValue(value,clazz));
		} else if (gteMatcher.find()) {
			value = gteMatcher.group(1);
			return Criteria.where(field).gte(convertFieldValue(value,clazz));
		}	
		else if (ninMatcher.find()) {
			value = ninMatcher.group(1);
			return Criteria.where(field).nin(Arrays.asList(value.split(",")).stream().map(val -> convertFieldValue(val,clazz)).collect(Collectors.toList()));
		} else if (inMatcher.find()) {
			value = inMatcher.group(1);
			return Criteria.where(field).in(Arrays.asList(value.split(",")).stream().map(val -> convertFieldValue(val,clazz)).collect(Collectors.toList()));
		} else if (existMatcher.find()) {
			value = existMatcher.group(1);
			return Criteria.where(field).exists(Boolean.valueOf((String)convertFieldValue(value,clazz)));
		} else {
			return Criteria.where(field).is(convertFieldValue(value,clazz));
		}	
	}
	
}
