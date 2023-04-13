package dashboard.sma.adapter.pa;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InfluxDbPersistence implements SmaDashboardPersistence {

  private final InfluxDBClient influxDBClient;

  public InfluxDbPersistence(InfluxDbPersistenceSettings settings) {
    this.influxDBClient =
        InfluxDBClientFactory.create(
            settings.getUrl(),
            settings.getToken().toCharArray(),
            settings.getOrg(),
            settings.getBucket());
  }

  @Override
  public void saveMeasurements(String plant, Map<String, Long> measurements) {

    WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

    long timestamp =
        LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli();
    var points =
        measurements.entrySet().stream()
            .map(
                stringLongEntry ->
                    Point.measurement(stringLongEntry.getKey())
                        .addField("value", stringLongEntry.getValue())
                        .time(timestamp, WritePrecision.MS))
            .toList();

    try {
      writeApi.writePoints(points);
      log.info(measurements.toString());
    } catch (Exception e) {
      log.error("{} during communication: {}", e.getClass().getSimpleName(), e.getMessage());
    }
  }
}
