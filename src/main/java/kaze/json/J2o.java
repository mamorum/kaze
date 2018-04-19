package kaze.json;

@FunctionalInterface  // Converter: Json to Object
public interface J2o { <T> T exec(String json, Class<T> toObj); }