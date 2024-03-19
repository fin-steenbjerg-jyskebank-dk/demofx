package dk.stonemountain.demo.demofx.bff;

import java.time.LocalDateTime;

import jakarta.json.bind.annotation.JsonbProperty;

public record HourlyPriceDTO(LocalDateTime time, @JsonbProperty("norlys-price") Double norlysPrice, @JsonbProperty("transport-and-tax") Double transportAndTax, @JsonbProperty("gros-norlys-price") Double grossNorlysPrice, PriceRecommendation recommendation) {
    public enum PriceRecommendation {
        BUY_POWER, BUY_GAS, BUY_ANY; 
    }
}
