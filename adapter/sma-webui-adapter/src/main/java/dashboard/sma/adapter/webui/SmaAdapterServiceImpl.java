package dashboard.sma.adapter.webui;

import dashboard.sma.adapter.pa.SmaDashboardPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmaAdapterServiceImpl implements SmaAdapterService {

  private final SmaDashboardPersistence smaDashboardPersistence;

  private final SmaWebUiClient smaClient;

  public SmaAdapterServiceImpl(
      SmaDashboardPersistence smaDashboardPersistence, SmaWebUiClient smaClient) {
    this.smaDashboardPersistence = smaDashboardPersistence;
    this.smaClient = smaClient;
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  log.info("Application shutdown");
                  smaClient.logout();
                }));
  }

  @Override
  @Scheduled(fixedRateString = "${sma.webui.adapter.fetchMeasures}")
  public void poll() {
    var values = this.smaClient.getValues();
    this.smaDashboardPersistence.saveMeasurements("roehre20", values);
  }
}
