package kaze;

import kaze.core.Build;

public class App {
	public static void start(String... pkg) {
	  (new Build()).server(pkg).start();
	}	
}