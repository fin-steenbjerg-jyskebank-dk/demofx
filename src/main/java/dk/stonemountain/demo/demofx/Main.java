package dk.stonemountain.demo.demofx;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		log.info("Starting up: {}", List.of(args));
		DemoApplication.main(new String[0]);
	}
}
