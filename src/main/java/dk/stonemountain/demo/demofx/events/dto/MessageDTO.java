package dk.stonemountain.demo.demofx.events.dto;

import java.time.ZonedDateTime;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public record MessageDTO(String author, String title, String content, @JsonbProperty("publishing-time") ZonedDateTime publishingTime) {
    @JsonbCreator
    public static MessageDTO create(String author, String title, String content, @JsonbProperty("publishing-time") ZonedDateTime publishingTime) {
        return new MessageDTO(author, title, content, publishingTime);
    }

}