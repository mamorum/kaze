package kaze.fw.lib;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Jackson {
	
	public static final ObjectMapper origin;
	
	static {
		origin = new ObjectMapper();
		origin.setVisibility(
			PropertyAccessor.FIELD,
			JsonAutoDetect.Visibility.ANY
		);
	}
	
	public static String toJson(Object obj) {
		try {
			return origin.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T toObj(String json, Class<T> obj) {
		try {
			return origin.readValue(json, obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T convert(Object val, Class<T> type) {
		return origin.convertValue(val, type);
	}
}
