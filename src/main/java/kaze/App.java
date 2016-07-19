package kaze;

import kaze.fw.Build;

public class App {
	public static void start(String... pkg) {
	  (new Build()).server(pkg).start();
	}	
}