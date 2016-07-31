package kaze;

import kaze.core.Build;

public class App {
	public static void start(String... pkgs) {
	  (new Build()).server(pkgs).start();
	}	
}