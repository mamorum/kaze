package it.http.model;

import java.util.List;

public class Person {
	private long id;
	public String name;
 	public List<String> langs;
 	
 	public Person() {}
 	public Person(long id, String name, List<String> langs) {
 		this.id = id;
 		this.name = name;
 		this.langs = langs;
 	}
 	
 	public long id() { return this.id; }
}
