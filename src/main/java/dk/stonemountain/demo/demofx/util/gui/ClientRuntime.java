package dk.stonemountain.demo.demofx.util.gui;

import dk.stonemountain.demo.demofx.Version;

public class ClientRuntime {
	private ClientRuntime() {
		// preventing instantiation
	}
	
	public static String getApplicationVersion() {	
		return Version.APP_VERSION;
	}
	
	public static String getApplicationBuildTime() {	
		return Version.APP_BUILD_TIME;
	}
	
	public static String getApplicationLogFolder() {
		String tempDir = System.getProperty("java.io.tmpdir");
		if (tempDir == null) {
			return "Not found";
		} else {
			tempDir = tempDir.trim().replace('\\', '/');
			if (!tempDir.endsWith("/")) {
				tempDir = tempDir + "/";
			}
			return tempDir + "logs/demofx";
		}
	}

	public static String getApplicationLog() {
		return getApplicationLogFolder() + "/application.log";
	}
}
