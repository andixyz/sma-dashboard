package dashboard.sma.adapter.webui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class Application {

  /**
   * Starts the application.
   *
   * @param args No arguments are used.
   */
  public static void main(String[] args) {
    printVersion();
    SpringApplication.run(Application.class);
  }

  @Bean
  public CommandLineRunner commandLineRunner(Optional<AdapterSettings> adapterSettings) {
    return (args) -> {
      log.info("Adapter started");
      if (adapterSettings.isEmpty()) {
        log.warn("No settings found!");
        return;
      }
    };
  }

  private static void printVersion() {
    Properties properties = new Properties();
    InputStream res = Application.class.getClassLoader().getResourceAsStream("git.properties");

    if (res == null) {
      // Uses System.out as the logger injection magic may fail
      System.out.println("GIT INFO - Not available, did you execute 'initialize' phase?");
      return;
    }

    try {
      properties.load(res);

      String sb =
          "GIT INFO Version="
              + properties.getProperty("git.build.version", "N/A")
              + ", Branch="
              + properties.getProperty("git.branch", "N/A")
              + ", BuildTime="
              + properties.getProperty("git.build.time", "N/A")
              + ", CommitID="
              + properties.getProperty("git.commit.id.describe", "N/A");
      // Uses System.out as the logger injection magic may fail
      System.out.println(sb);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
