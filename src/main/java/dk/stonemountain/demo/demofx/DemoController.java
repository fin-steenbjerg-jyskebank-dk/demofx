package dk.stonemountain.demo.demofx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.util.gui.ClientRuntime;
import dk.stonemountain.demo.demofx.util.gui.DialogHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DemoController {
	private static final String SYSTEM_FAILURE_TITLE = "System Failure";
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);

	@FXML private Label time;

	@FXML 
	void doQuit(ActionEvent event) {
		DemoApplication.getApplication().quit();
	}

	@FXML
	void doAbout(ActionEvent event) {
		StringBuilder contentText = new StringBuilder()
				.append("You are currently using the Cluster Configurator application for tailoring your use of the Jyske Bank OCP Clusters.")
				.append("Application log folder: " + ClientRuntime.getApplicationLogFolder() + "\n\n")
				.append("Fill free to contact fin.steenbjerg@jyskebank.dk or vagt56@jyskebank.dk");
		DialogHelper.showInformationDialog(time.getScene().getWindow(), "About Cluster Configurator", "You are using version: " + ClientRuntime.getApplicationVersion(), contentText.toString(), "Failed to show about dialog");
	}

	@FXML
	void initialize() {
		log.debug("initializing");
	}

	public void failureOccurred(Throwable e) {
		log.error(SYSTEM_FAILURE_TITLE, e);
		DialogHelper.showErrorDialog(time.getScene().getWindow(), SYSTEM_FAILURE_TITLE, "Failed to fetch clusters", e.getMessage(), "Could not show error dialog");
		Platform.exit();
	}
}
