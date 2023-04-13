package dashboard.sma.adapter.pa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@Configuration
@ConfigurationProperties(prefix = "sma.pa.influxdb")
public class InfluxDbPersistenceSettings {

  private String token;
  private String url;
  private String org;
  private String bucket;
}
