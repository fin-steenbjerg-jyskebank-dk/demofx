package dk.stonemountain.demo.demofx.messages;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.util.gui.DialogHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;

public class MessageCell extends ListCell<Message> {
    private static final Logger logger = LoggerFactory.getLogger(MessageCell.class);

    @FXML private Label author;
    @FXML private TextArea content;
    @FXML private Label title;
    @FXML private Label updated;

    private final Node node;
    private Optional<Message> item = Optional.empty();
    private final Consumer<Message> messageDeleteConsumer;

    public MessageCell(Consumer<Message> messageDeleteConsumer) {
        super();
        this.messageDeleteConsumer = messageDeleteConsumer;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setController(this);
			loader.setLocation(this.getClass().getResource("/fxml/message-cell.fxml"));
			node = loader.load(this.getClass().getResourceAsStream("/fxml/message-cell.fxml"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load fxml", e);
		}
    }

    @FXML
    public void init() {
    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            this.item = Optional.empty();
            setGraphic(null);
        } else {
            this.item = Optional.of(item);
            author.setText(item.getAuthor());
            updated.setText(item.getUpdatingTime().toString());
            content.setText(item.getText());
            title.setText(item.getTitle());
            setGraphic(node);
        }
    }

    @FXML
    public void doDelete(ActionEvent event) {
        logger.info("Deleting message {}", item);
        DialogHelper.showConfirmationDialog(this.getScene().getWindow(), "Do you really want to delete message with title '" + item.get().getTitle() + "'?", "Could not delete message", () -> deleteMessage(item.get()));
    }

    private void deleteMessage(Message msg) {
        messageDeleteConsumer.accept(msg);
    }

    @FXML
    public void doView(ActionEvent event) {
        logger.info("Viewing message {}", item);
    }
}