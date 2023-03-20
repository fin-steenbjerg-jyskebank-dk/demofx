package dk.stonemountain.demo.demofx.authentication;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.ApplicationContainer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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

public class UserDialog extends Dialog<Void> {
	private static final Logger logger = LoggerFactory.getLogger(UserDialog.class);

	@FXML private TextField accessTokenExpiration;
    @FXML private TextField email;
	@FXML private TextField refreshTokenExpiration;
	@FXML private TextField userId;
	@FXML private TextField userName;

	public UserDialog(Window owner) {
		Node node;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			loader.setLocation(this.getClass().getResource("/fxml/user-dialog.fxml"));
			node = loader.load(this.getClass().getResourceAsStream("/fxml/user-dialog.fxml"));
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
		pane.setHeaderText("View User Information");

		setResizable(false);
		setTitle("User Authentication Information");
		setGraphic(new ImageView(this.getClass().getResource("/icons/about_64.png").toString()));
		setHeaderText("User Info");
		setDialogPane(pane);

		Button okButton = (Button) pane.lookupButton(ButtonType.OK);
		// okButton.setGraphic(new ImageView(this.getClass().getResource("/icons/about_16.png").toString()));
		okButton.setText("Close");
		
		// setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? Boolean.TRUE : null);

		userId.setText(ApplicationContainer.getInstance().getUser().getUserId());
		userName.setText(ApplicationContainer.getInstance().getUser().getUserName());
		email.setText(ApplicationContainer.getInstance().getUser().getEmail());
		accessTokenExpiration.textProperty().bind(Bindings.createStringBinding(() -> ApplicationContainer.getInstance().getUser().getAccessTokenExpiration().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), ApplicationContainer.getInstance().getUser().getAccessTokenExpirationProperty()));
		refreshTokenExpiration.textProperty().bind(Bindings.createStringBinding(() -> ApplicationContainer.getInstance().getUser().getRefreshTokenExpiration().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), ApplicationContainer.getInstance().getUser().getRefreshTokenExpirationProperty()));

		// IconHelper.patchIconPath(viewLogButton);
		Platform.runLater(okButton::requestFocus);
	}	
}