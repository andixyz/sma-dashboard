package dashboard.sma.adapter.webui;

import java.util.HashMap;
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
@ConfigurationProperties(prefix = "sma.webui")
public class AdapterSettings {

  public static final String ENDPOINT_LOGIN = "login";
  public static final String ENDPOINT_LOGOUT = "logout";
  public static final String ENDPOINT_VALUES = "values";
  public static final String ENDPOINT_LOGGER = "logger";

  private HashMap<String, String> values;
  private HashMap<String, String> endpoints;

  private String role;
  private String passwd;
}
