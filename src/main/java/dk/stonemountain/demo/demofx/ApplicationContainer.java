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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ApplicationContainer {	
	private static final Logger log = LoggerFactory.getLogger(ApplicationContainer.class);	
	private static ApplicationContainer instance = new ApplicationContainer();

	private final UserPreferences userPreferences = new UserPreferences();
	private final VersionInformation version = new VersionInformation();
	private final BooleanProperty updatedVersionReady = new SimpleBooleanProperty(); 
	private final StringProperty updatedVersion = new SimpleStringProperty(); 
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
	
	public StringProperty updatedVersionProperty() {
		return updatedVersion;
	}

	public BooleanProperty updatedVersionReadyProperty() {
		return updatedVersionReady;
	}

	public void updateVersion(VersionInformation info) {
		version.setMustBeUpdated(info.getMustBeUpdated());
		version.setNewSha(info.getNewSha());
		version.setNewVersion(info.getNewVersion());
		version.setNewerVersionAvailable(info.getNewerVersionAvailable());
		version.setNewReleaseNote(info.getNewReleaseNote());
		version.setNewReleaseTime(info.getNewReleaseTime());
	}

	public void updatedVersionReady(Path file) {
		updatedVersionReady.set(true);
		updatedVersion.set(version.getNewVersion());
		newSwVersion.set(file);
	}
}
