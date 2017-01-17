package com.alpha.util;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectJsonMapper {

	public static Object jsonObjectMapper(String jsonValueAsString, Class<?> classToMap) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonValueAsString, classToMap);
	}
}
