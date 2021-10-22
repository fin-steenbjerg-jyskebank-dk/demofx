package dk.stonemountain.demo.demofx.about;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.DemoApplication;
import dk.stonemountain.demo.demofx.util.gui.ClientRuntime;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

public class AboutDialog extends Dialog<Void> {
	private static final Logger logger = LoggerFactory.getLogger(AboutDialog.class);

	@FXML TextField log;
	@FXML TextField version;
	@FXML TextField buildTime;

	public AboutDialog(Window owner) {
		Node node;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			loader.setLocation(this.getClass().getResource("/fxml/about-dialog.fxml"));
			node = loader.load(this.getClass().getResourceAsStream("/fxml/about-dialog.fxml"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load fxml", e);
		}
		initOwner(owner);
		getDialogPane().setContent(node);
	}

	@FXML
	public void initialize() {
		logger.trace("Dialog initialize");
		final DialogPane pane = getDialogPane();
		pane.getButtonTypes().addAll(ButtonType.OK);
		pane.setHeaderText("About DemoFX");

		setResizable(false);
		setTitle("DemoFX About Information");
		setGraphic(new ImageView(this.getClass().getResource("/icons/about_64.png").toString()));
		setHeaderText("DemoFX About Information");
		setDialogPane(pane);

		Button okButton = (Button) pane.lookupButton(ButtonType.OK);
		okButton.setGraphic(new ImageView(this.getClass().getResource("/icons/tick_32.png").toString()));
		okButton.setText("");

//		setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? authenticationData : null);
		
		log.setText(ClientRuntime.getApplicationLog());
		version.setText(ClientRuntime.getApplicationVersion());
		buildTime.setText(ClientRuntime.getApplicationBuildTime());
		 
		Platform.runLater(() -> okButton.requestFocus());
	}

	@FXML
	public void doShowLogFolder(ActionEvent e) {
		File logFile = new File(ClientRuntime.getApplicationLog());
		logger.trace("Showing log file: {}", logFile.toURI().toString());
		DemoApplication.getApplication().getHostServices().showDocument(logFile.toURI().toString());
//		java.awt.Desktop.getDesktop().browseFileDirectory(logFile);
	}
	
	@FXML
	public void doReportIssue(ActionEvent e) {
		new IssueDialog(getOwner()).showAndWait();
	}
}