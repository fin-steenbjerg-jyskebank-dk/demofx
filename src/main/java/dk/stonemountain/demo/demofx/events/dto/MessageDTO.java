package dk.stonemountain.demo.demofx.events.dto;

import java.time.ZonedDateTime;

import javax.json.bind.annotation.JsonbProperty;

public class MessageDTO {
    public String author;
    public String title;
    public String content;
    @JsonbProperty("publishing-time")
    public ZonedDateTime publishingTime = ZonedDateTime.now();
}