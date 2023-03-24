package dk.stonemountain.demo.demofx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.about.AboutDialog;
import dk.stonemountain.demo.demofx.about.IssueDialog;
import dk.stonemountain.demo.demofx.authentication.AccessTokenResponse;
import dk.stonemountain.demo.demofx.authentication.IdToken;
import dk.stonemountain.demo.demofx.authentication.TokenExtractor;
import dk.stonemountain.demo.demofx.authentication.UserDialog;
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
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class DemoController {
	private static final String SYSTEM_FAILURE_TITLE = "System Failure";
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@FXML private Label time;
	@FXML private BorderPane applicationPane;
	@FXML private Button updateButton;
	@FXML private Button userInfoButton;
	@FXML private ListView<Message> messagesList;
	@FXML private MenuItem loginMenuItem;
	@FXML private MenuItem logoutMenuItem;
	
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

		// User
		userInfoButton.disableProperty().bind(Bindings.createBooleanBinding(() -> !ApplicationContainer.getInstance().getUser().getLoggedIn().booleanValue(), ApplicationContainer.getInstance().getUser().getLoggedInProperty()));
		userInfoButton.textProperty().bind(Bindings.createStringBinding(() -> ApplicationContainer.getInstance().getUser().getLoggedIn().booleanValue() ? ApplicationContainer.getInstance().getUser().getUserId() + "/" + ApplicationContainer.getInstance().getUser().getUserName() : "Not Signed In", ApplicationContainer.getInstance().getUser().getLoggedInProperty(), ApplicationContainer.getInstance().getUser().getUserIdProperty(), ApplicationContainer.getInstance().getUser().getUserNameProperty()));


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

	@FXML
	void doLogin(ActionEvent event) {
		try {
			var container = ApplicationContainer.getInstance();
			var authUrl = container.getAuthManager().initiateAuthenticationFlow(container.getCurrentBackend(), this::authenticated);
			DemoApplication.getApplication().getHostServices().showDocument(authUrl);
		} catch (Exception e) {
			log.error("Failure showing link", e);
			DialogHelper.showErrorDialog(updateButton.getScene().getWindow(), "System Failure", "Failure when lauching link", e.getMessage(), "Could not show error dialog");
		}
	}

	@FXML
	void doLogout(ActionEvent event) {
		var user = ApplicationContainer.getInstance().getUser();
		user.setAccessToken("");
		user.setRefreshToken("");
		user.setUserId("Not Signed In");
		user.setUserName("Unauthenticated");
		user.setEmail("Unknown");
		user.setLoggedIn(false);

		log.debug("Adding user : {}", user);
	}

	private void authenticated(AccessTokenResponse response) {
		log.info("Authenticated: {}", response);

		LocalDateTime accessTokenExpiration = LocalDateTime.now().plusSeconds(response.expiresIn());
		LocalDateTime refreshTokenExpiration = LocalDateTime.now().plusSeconds(response.refreshExpiresIn());
		IdToken idToken = new TokenExtractor().extract(response.idToken());
		log.info("Id token: {}", idToken);

		var user = ApplicationContainer.getInstance().getUser();
		user.setAccessToken(response.token());
		user.setRefreshToken(response.refreshToken());
		user.setAccessTokenExpiration(accessTokenExpiration);
		user.setRefreshTokenExpiration(refreshTokenExpiration);
		user.setUserId(idToken.userId());
		user.setUserName(idToken.userName());
		user.setEmail(idToken.email());
		user.setLoggedIn(true);

		log.debug("Adding user : {}", user);
	}

	@FXML
	void doShowUser(ActionEvent event) {
		new UserDialog(userInfoButton.getScene().getWindow()).showAndWait();			
	}

	@FXML
	void doSelectDefaultCss(ActionEvent event) {
		var scene = applicationPane.getScene();
		log.info("Current stylesheet: {}", scene.getStylesheets());
		scene.getStylesheets().clear();
	}

	@FXML
	void doSelectDarkCss(ActionEvent event) {
		var scene = applicationPane.getScene();
		log.info("Current stylesheet: {}", scene.getStylesheets());
		scene.getStylesheets().clear();
		scene.getStylesheets().add("dark.css");
	}
}
