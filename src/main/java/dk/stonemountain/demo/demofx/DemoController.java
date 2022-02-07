package dk.stonemountain.demo.demofx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.about.AboutDialog;
import dk.stonemountain.demo.demofx.about.IssueDialog;
import dk.stonemountain.demo.demofx.installer.UpdateDialog;
import dk.stonemountain.demo.demofx.messages.Message;
import dk.stonemountain.demo.demofx.messages.MessageCell;
import dk.stonemountain.demo.demofx.messages.MessageDialog;
import dk.stonemountain.demo.demofx.util.gui.DialogHelper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class DemoController {
	private static final String SYSTEM_FAILURE_TITLE = "System Failure";
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@FXML private Label time;
	@FXML private BorderPane applicationPane;
	@FXML private Button updateButton;
	@FXML private ListView<Message> messagesList;
	
	final Timeline timeline = new Timeline();

	public DemoController() {
	}

	@FXML
	void doQuit(ActionEvent event) {
		DemoApplication.getApplication().quit();
	}

	@FXML
	void doAbout(ActionEvent event) {
		log.trace("Showing about dialog");
		new AboutDialog(applicationPane.getScene().getWindow()).showAndWait();
	}

	@FXML
	void doReportIssue(ActionEvent event) {
		log.trace("Showing issue dialog");
		new IssueDialog(applicationPane.getScene().getWindow()).showAndWait();
	}
	
	@FXML
    void installNewVersion(ActionEvent event) {
		log.info("Installing new version: {}", ApplicationContainer.getInstance().getVersion());
		new UpdateDialog(updateButton.getScene().getWindow()).showAndWait().ifPresent(mustBeUpdated -> {
			ApplicationContainer.getInstance().getInstaller().install(ApplicationContainer.getInstance().getNewSwVersion().get());
		});
    }

	@FXML
	void initialize() {
		log.debug("initializing");
		
		log.trace("Application Pane: {}", applicationPane);
		log.trace("Application Pane center content: {}", applicationPane.getCenter());
		// ImageView imgView = (ImageView) applicationPane.getCenter();
		// applicationPane.setCenter(IconHelper.patchIconPath(imgView));
		
		// Installer stuff
		updateButton.disableProperty().bind(Bindings.createBooleanBinding(() -> !ApplicationContainer.getInstance().getVersion().getNewerVersionAvailable().booleanValue(), ApplicationContainer.getInstance().getVersion().newerVersionAvailableProperty()));
		updateButton.textProperty().bind(Bindings.createStringBinding(() -> ApplicationContainer.getInstance().getVersion().getNewerVersionAvailable().booleanValue() ? "Update to " + ApplicationContainer.getInstance().getVersion().getNewVersion() : "No Updates", ApplicationContainer.getInstance().getVersion().newerVersionAvailableProperty(), ApplicationContainer.getInstance().getVersion().newVersionProperty()));
		ApplicationContainer.getInstance().getVersion().mustBeUpdatedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.booleanValue()) {
				installNewVersion(null);
			}
		});

		// Content
		messagesList.setItems(ApplicationContainer.getInstance().getMessages());
		messagesList.setCellFactory(p -> new MessageCell(this::deleteMessage));

		// Start the update of the time field
		setTime();
		timeline.setCycleCount(Animation.INDEFINITE);
//		timeline.setAutoReverse(true);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5), e -> setTime()));
		timeline.play();
	}

	@FXML 
	void doCreateMessage(ActionEvent event) {
		Message msg = new Message();
		msg.setPublishingTime(LocalDateTime.now());
		msg.setUpdatingTime(LocalDateTime.now());
		msg.setAuthor("Fin Steenbjerg");
		new MessageDialog(applicationPane.getScene().getWindow(), msg).showAndWait().ifPresent(m -> ApplicationContainer.getInstance().getMessages().add(m));
	}

	private void deleteMessage(Message m) {
		log.info("Deleting Message {}", m);
		boolean removed = ApplicationContainer.getInstance().getMessages().remove(m);
		log.info("Message {} deleted {}", m, removed);
	}

	private void setTime() {
		time.setText(LocalDateTime.now().format(formatter));
	}

	public void failureOccurred(Throwable e) {
		log.error(SYSTEM_FAILURE_TITLE, e);
		DialogHelper.showErrorDialog(time.getScene().getWindow(), SYSTEM_FAILURE_TITLE, "Failed to fetch clusters", e.getMessage(), "Could not show error dialog");
		Platform.exit();
	}
}
