package dk.stonemountain.demo.demofx.bff;

import java.util.List;

import jakarta.json.bind.annotation.JsonbProperty;

public record HourReportDTO (
    @JsonbProperty("hourly-prices") List<HourlyPriceDTO> hourlyPrices, 
    @JsonbProperty("gross-max-price-per-kwh-kr") Double grossMaxKr) {
}
