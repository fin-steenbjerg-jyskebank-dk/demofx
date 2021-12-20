package dk.stonemountain.demo.demofx;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.installer.PackageInstaller;
import dk.stonemountain.demo.demofx.installer.VersionInformation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ApplicationContainer {	
	private static final Logger log = LoggerFactory.getLogger(ApplicationContainer.class);	
	private static ApplicationContainer instance = new ApplicationContainer();

	private final UserPreferences userPreferences = new UserPreferences();
	private final BooleanProperty updatedVersionReady = new SimpleBooleanProperty(); 
	private final VersionInformation version = new VersionInformation();
	private final ObjectProperty<Path> newSwVersion = new SimpleObjectProperty<>(); 
	private final PackageInstaller installer = new PackageInstaller();
	private final Backend currentBackend = Backend.PRODUCTION;

	public static ApplicationContainer getInstance() {
		return instance;
	}
	
	private ApplicationContainer() {
		// to prevent instantiation)
		log.info("Starting application container");
		installer.startInstaller();
	}

	public Backend getCurrentBackend() {
		return currentBackend;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public PackageInstaller getInstaller() {
		return installer;
	}

	public VersionInformation getVersion() {
		return version;
	}

	public ObjectProperty<Path> getNewSwVersion() {
		return newSwVersion;
	}
	
	public BooleanProperty updatedVersionReadyProperty() {
		return updatedVersionReady;
	}

	public void updatedVersionReady(VersionInformation info, Path file) {
		updatedVersionReady.set(true);
		newSwVersion.set(file);

		version.setNewSha(info.getNewSha());
		version.setNewVersion(info.getNewVersion());
		version.setNewReleaseNote(info.getNewReleaseNote());
		version.setNewerVersionAvailable(info.getNewerVersionAvailable());
		version.setNewReleaseTime(info.getNewReleaseTime());
		version.setMustBeUpdated(info.getMustBeUpdated());
	}
}
