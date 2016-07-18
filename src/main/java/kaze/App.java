package kaze;

import kaze.fw.Build;
import kaze.fw.Handler;

public class App {
	public static void start(String... pkg) {
		Handler handler = Build.make(pkg);
		handler.listen();
	}	
}