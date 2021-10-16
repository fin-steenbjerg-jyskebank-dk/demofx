package dk.stonemountain.demo.demofx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContainer {	
	private static final Logger log = LoggerFactory.getLogger(ApplicationContainer.class);	
	private static ApplicationContainer instance = new ApplicationContainer();
	private UserPreferences userPreferences = new UserPreferences();

	public static ApplicationContainer getInstance() {
		return instance;
	}
	
	private ApplicationContainer() {
		// to prevent instantiation
		log.info("Starting application container");
	}
		
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}	
}
