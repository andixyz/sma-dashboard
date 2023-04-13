package dashboard.sma.adapter.pa;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SmaDashboardPersistence {
  void saveMeasurements(String plant, Map<String, Long> measurements);
}
