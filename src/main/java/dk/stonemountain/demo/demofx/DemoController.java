package dk.stonemountain.demo.demofx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.about.AboutDialog;
import dk.stonemountain.demo.demofx.about.IssueDialog;
import dk.stonemountain.demo.demofx.util.gui.DialogHelper;
import dk.stonemountain.demo.demofx.util.gui.IconHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class DemoController {
	private static final String SYSTEM_FAILURE_TITLE = "System Failure";
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);

	@FXML private Label time;
	@FXML private BorderPane applicationPane;

	@FXML 
	void doQuit(ActionEvent event) {
		DemoApplication.getApplication().quit();
	}

	@FXML
	void doAbout(ActionEvent event) {
		new AboutDialog(applicationPane.getScene().getWindow()).showAndWait();
	}

	@FXML
	void doReportIssue(ActionEvent event) {
		new IssueDialog(applicationPane.getScene().getWindow()).showAndWait();
	}
	
	@FXML
	void initialize() {
		log.debug("initializing");
		
		log.trace("Application Pane: {}", applicationPane);
		log.trace("Application Pane center content: {}", applicationPane.getCenter());
		ImageView imgView = (ImageView) applicationPane.getCenter();
		applicationPane.setCenter(IconHelper.patchIconPath(imgView));
	}

	public void failureOccurred(Throwable e) {
		log.error(SYSTEM_FAILURE_TITLE, e);
		DialogHelper.showErrorDialog(time.getScene().getWindow(), SYSTEM_FAILURE_TITLE, "Failed to fetch clusters", e.getMessage(), "Could not show error dialog");
		Platform.exit();
	}
}
