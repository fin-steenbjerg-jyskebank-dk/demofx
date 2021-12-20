package dk.stonemountain.demo.demofx;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		log.info("Starting up: {}", List.of(args));
		log.info("Code source: {}", Main.class.getProtectionDomain().getCodeSource().getLocation());
		
		log.info("Process Handle: {}", ProcessHandle.current());
		log.info("Process Handle Info: {}", ProcessHandle.current().info());
		log.info("Process Handle Info. Command = {}, Command line = {}", ProcessHandle.current().info().command(), ProcessHandle.current().info().commandLine());
		DemoApplication.main(new String[0]);
	}
}