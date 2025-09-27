package dk.stonemountain.demo.demofx;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		log.info("Starting up: command = {}, command line = {}", ProcessHandle.current().info().command(), ProcessHandle.current().info().commandLine());

		try {
			Class<?> cls = Class.forName("com.sun.glass.ui.gtk.GtkView");
			log.info("Loaded GtkView: {}", cls);
			Arrays.stream(cls.getMethods()).forEach(m -> {
				log.info("Method: {}", m);
			});
		} catch (ClassNotFoundException e) {
			log.error("Failed to load GtkView", e);
		}
		DemoApplication.main(new String[0]);
	}
}