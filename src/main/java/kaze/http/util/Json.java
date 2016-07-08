package kaze.http.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	
	private static final ObjectMapper jksn = new ObjectMapper();
	
	public static String of(Object obj) {
		try {
			return jksn.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T toObj(String json, Class<T> obj) {
		try {
			return jksn.readValue(json, obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
