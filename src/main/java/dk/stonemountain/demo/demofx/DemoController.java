package dk.stonemountain.demo.demofx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.about.AboutDialog;
import dk.stonemountain.demo.demofx.about.IssueDialog;
import dk.stonemountain.demo.demofx.installer.PackageInstaller;
import dk.stonemountain.demo.demofx.installer.UpdateDialog;
import dk.stonemountain.demo.demofx.util.gui.DialogHelper;
import dk.stonemountain.demo.demofx.util.gui.IconHelper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class DemoController {
	private static final String SYSTEM_FAILURE_TITLE = "System Failure";
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@FXML private Label time;
	@FXML private BorderPane applicationPane;
	@FXML private Button updateButton;

	final Timeline timeline = new Timeline();

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
		ImageView imgView = (ImageView) applicationPane.getCenter();
		applicationPane.setCenter(IconHelper.patchIconPath(imgView));
		
		// Installer stuff
		updateButton.disableProperty().bind(Bindings.createBooleanBinding(() -> !ApplicationContainer.getInstance().getVersion().getNewerVersionAvailable().booleanValue(), ApplicationContainer.getInstance().getVersion().newerVersionAvailableProperty()));
		updateButton.textProperty().bind(Bindings.createStringBinding(() -> ApplicationContainer.getInstance().getVersion().getNewerVersionAvailable().booleanValue() ? "New Update Available" : "Newest Version Installed", ApplicationContainer.getInstance().getVersion().newerVersionAvailableProperty()));
		ApplicationContainer.getInstance().getVersion().mustBeUpdatedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.booleanValue()) {
				// Show dialog
			}
		});

		// Start the update of the time field
		setTime();
		timeline.setCycleCount(Animation.INDEFINITE);
//		timeline.setAutoReverse(true);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5), e -> setTime()));
		timeline.play();
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
