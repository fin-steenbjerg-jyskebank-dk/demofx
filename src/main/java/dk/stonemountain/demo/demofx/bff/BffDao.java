package dk.stonemountain.demo.demofx.bff;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import dk.stonemountain.demo.demofx.util.jaxrs.JsonbHelper;
import javafx.scene.chart.XYChart;

public class BffDao {
    public HourlyPrices getHourlyPrices(Double fuelPrice) {
        try (var is = BffDao.class.getResourceAsStream("/test/data.json")) {
            var json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            HourReportDTO dto = JsonbHelper.fromJson(json, HourReportDTO.class);
            var netto = dto.hourlyPrices().stream()
                .map(d -> new XYChart.Data<LocalDateTime,Double>(d.time(), d.norlysPrice()))
                .toList();

            var tax = dto.hourlyPrices().stream()
                .map(d -> new XYChart.Data<LocalDateTime, Double>(d.time(), d.transportAndTax()))
                .toList();

            return new HourlyPrices(netto, tax);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch hourly report", e);
        }
    }

    public record HourlyPrices(List<XYChart.Data<LocalDateTime, Double>> norlysSpotPrice, List<XYChart.Data<LocalDateTime, Double>> taxAndPrice) {
    }
}