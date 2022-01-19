package dk.stonemountain.demo.demofx.messages;

import java.time.LocalDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty author = new SimpleStringProperty();
    private StringProperty text = new SimpleStringProperty();
    private ObjectProperty<LocalDateTime> publishingTime = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> updatingTime = new SimpleObjectProperty<>();

    public Message() {
    }

    public Message(String title, String auther, String text) {
        this.title.set(title);
        this.author.set(auther);
        this.text.set(text);
        this.publishingTime.set(LocalDateTime.now());
        this.updatingTime.set(LocalDateTime.now());
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty textProperty() {
        return text;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public ObjectProperty<LocalDateTime> publishingTimeProperty() {
        return publishingTime;
    }

    public LocalDateTime getPublishingTime() {
        return publishingTime.get();
    }

    public void setPublishingTime(LocalDateTime publishingTime) {
        this.publishingTime.set(publishingTime);
    }

    public ObjectProperty<LocalDateTime> updatingTimeProperty() {
        return updatingTime;
    }

    public LocalDateTime getUpdatingTime() {
        return updatingTime.get();
    }

    public void setUpdatingTime(LocalDateTime updatingTime) {
        this.updatingTime.set(updatingTime);
    }

    @Override
    public String toString() {
        return "Message [author=" + author + ", publishingTime=" + publishingTime + ", title=" + title
                + ", updatingTime=" + updatingTime + ", text=" + text + "]";
    }
}
