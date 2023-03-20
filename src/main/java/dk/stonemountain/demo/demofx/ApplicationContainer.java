package dk.stonemountain.demo.demofx;

import java.nio.file.Path;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.authentication.AuthenticationManager;
import dk.stonemountain.demo.demofx.authentication.User;
import dk.stonemountain.demo.demofx.events.EventsReceiverService;
import dk.stonemountain.demo.demofx.events.HttpSseClient;
import dk.stonemountain.demo.demofx.events.dto.MessageDTO;
import dk.stonemountain.demo.demofx.installer.PackageInstaller;
import dk.stonemountain.demo.demofx.installer.VersionInformation;
import dk.stonemountain.demo.demofx.messages.Message;
import dk.stonemountain.demo.demofx.util.jaxrs.JsonbHelper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ApplicationContainer {	
	private static final Logger log = LoggerFactory.getLogger(ApplicationContainer.class);	
	private static ApplicationContainer instance = new ApplicationContainer();

	private final UserPreferences userPreferences = new UserPreferences();
	private final BooleanProperty updatedVersionReady = new SimpleBooleanProperty(); 
	private final VersionInformation version = new VersionInformation();
	private final ObjectProperty<Path> newSwVersion = new SimpleObjectProperty<>(); 
	private final PackageInstaller installer = new PackageInstaller();
	private final Backend currentBackend = Backend.PRODUCTION;
	private final EventsReceiverService eventsReceiver = new EventsReceiverService(this::dispatchEvent, this::handleLostConnection);
	private final ObservableList<Message> messages = FXCollections.observableArrayList();
	private final AuthenticationManager authManager = new AuthenticationManager();
	private final User user = new User();

	public static ApplicationContainer getInstance() {
		return instance;
	}
	
	private ApplicationContainer() {
		// to prevent instantiation)
	}
	
	public void start() {
		log.info("Starting application container");
		installer.startInstaller();
		eventsReceiver.start();
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

	public AuthenticationManager getAuthManager() {
		return authManager;
	}

	public User getUser() {
		return user;
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

	public ObservableList<Message> getMessages() {
		return messages;
	}

	private void dispatchEvent(HttpSseClient.Event event) {
		log.info("Event received: {}", event);
		if("message".equals(event.event)) {
			var m = JsonbHelper.fromJson(event.data, MessageDTO.class);
			var msg = new Message();
			msg.setAuthor(m.author());
			msg.setPublishingTime(m.publishingTime().toLocalDateTime());
			msg.setUpdatingTime(m.publishingTime().toLocalDateTime());
			msg.setTitle(m.title());
			msg.setText(m.content());
			messages.add(msg);
		}
	}

	private void handleLostConnection(LocalDateTime lastActivity) {
		log.info("Event Connection lost at {}", lastActivity);
		eventsReceiver.start();
	}
}
