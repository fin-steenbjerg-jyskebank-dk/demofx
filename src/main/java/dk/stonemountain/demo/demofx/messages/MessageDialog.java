package dk.stonemountain.demo.demofx.messages;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.events.dto.MessageDTO;
import dk.stonemountain.demo.demofx.util.jaxrs.JsonbHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

public class MessageDialog extends Dialog<Message> {
	private static final Logger logger = LoggerFactory.getLogger(MessageDialog.class);

    @FXML private TextField msgAuthor;
    @FXML private TextArea msgContent;
    @FXML private TextField msgTitle;

    private final Message msg;

    public MessageDialog(Window owner, Message msg) {
        this.msg = msg;
		Node node;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			loader.setLocation(this.getClass().getResource("/fxml/message-dialog.fxml"));
			node = loader.load(this.getClass().getResourceAsStream("/fxml/message-dialog.fxml"));
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
		pane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		pane.setHeaderText("Create Message");

		setResizable(false);
		setTitle("DemoFX: Create Message");
		setGraphic(new ImageView(this.getClass().getResource("/icons/message_64.png").toString()));
		setHeaderText("Message Creation");
		setDialogPane(pane);

		Button okButton = (Button) pane.lookupButton(ButtonType.OK);
		// okButton.setGraphic(new ImageView(this.getClass().getResource("/icons/install_16.png").toString()));
		okButton.setText("Create");
		
		setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? msg : null);

		msg.titleProperty().bind(msgTitle.textProperty());
		msg.authorProperty().bind(msgAuthor.textProperty());
		msg.textProperty().bind(msgContent.textProperty());

		// IconHelper.patchIconPath(viewLogButton);
		 
		var newMsg = new MessageDTO("Fin Steenbjerg", "This is a title", "This is a content", ZonedDateTime.now());
		logger.info("Test: {}", JsonbHelper.toJson(newMsg));

		Platform.runLater(() -> okButton.requestFocus());
	}	
}

