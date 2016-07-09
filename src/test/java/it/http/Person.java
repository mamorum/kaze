package it.http;

import java.util.List;

public class Person {
	public long id;
	public String name;
 	public List<String> langs;
 	
 	public Person() {}
 	public Person(long id, String name, List<String> langs) {
 		this.id = id;
 		this.name = name;
 		this.langs = langs;
 	}
}
