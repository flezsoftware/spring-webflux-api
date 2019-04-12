package pl.flez.spring.webflux.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.MultiValueMap;
public class PageAndSortResolver {	
	private final static String pageKey = "page";
	private final static String sizeKey = "size";
	private final static String sortKey = "sort";
	private final static String ascKey = "asc";
	private final static String descKey = "desc";
	private final static String splitSortKey = ",";
	private static Integer page;
	private static Integer size;
	
	@Value("${spring.data.web.pageable.page-parameter:0}")
	public void setPage(Integer page) {
		PageAndSortResolver.page = page;
	}
	@Value("${spring.data.web.pageable.size-parameter:20}") 
	public  void setSize(Integer size) {
		PageAndSortResolver.size = size;
	}
	public static Pageable createPageable(MultiValueMap<String, String> parameters) {
		page = getPageableParameter(parameters,pageKey,page);
		size = getPageableParameter(parameters,sizeKey,size);	
		if(parameters.containsKey(sortKey)) {
			final List<Order> orders = getSortParameters(parameters);
			if(!orders.isEmpty()) {
				return PageRequest.of(page, size, Sort.by(orders));
			}
		}			
		return PageRequest.of(page, size);
	}
	
	private static Integer getPageableParameter(MultiValueMap<String, String> parameters, String paramKey, Integer initialValue) {
		if(parameters.containsKey(paramKey)) {
			final String value = parameters.getFirst(paramKey);
			if(NumberUtils.isParsable(value)) {
				return Integer.valueOf(parameters.getFirst(paramKey));
			}			
		}
		return initialValue;
	}
	
	private static List<Order> getSortParameters(MultiValueMap<String, String> parameters){
		final List<Order> orders = new ArrayList<Order>();
		parameters.get(sortKey).stream().forEach(sort ->{
			if(sort.contains(",")) {
				final String [] sortParams = sort.split(splitSortKey);
				if(sortParams.length > 1) {
					if(sortParams[1].equals(ascKey)) {
						orders.add(Order.asc(sortParams[0]));
					} else
				    if (sortParams[1].equals(descKey)) {
				    	orders.add(Order.desc(sortParams[0]));
					}
				}
			}			
		});
		return orders;
	}
	
}
