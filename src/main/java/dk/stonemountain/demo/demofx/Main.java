package dk.stonemountain.demo.demofx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		log.info("Starting up");
		DemoApplication.main(new String[0]);
	}
}
