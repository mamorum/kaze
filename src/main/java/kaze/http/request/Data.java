package kaze.http.request;

public class Data<T> {
	
	private T binded;
	public Data(T binded) { this.binded = binded; }
	
	public T get() {
		return this.binded;
	}
	
	// TODO implement validation.
	public T valid() {
		return this.binded;
	}
}
