package kaze.http.req;

public class Data<T> {
	
	private T src;
	public Data(T src) { this.src = src; }
	
	public T get() {
		return this.src;
	}
	
	// TODO implement validation.
	public T valid() {
		return this.src;
	}
}
