package dashboard.sma.adapter.webui;

/** Service interface as central point for the adatper. */
public interface SmaAdapterService {

  /** Polls the data from the SMA inverter and stores the data in our defined DBMS */
  void poll();
}
