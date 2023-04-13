package dashboard.sma.adapter.webui;

public class SmaWebUiClientException extends RuntimeException {
  public SmaWebUiClientException(Exception e) {
    super(e);
  }

  public SmaWebUiClientException(String message) {
    super(message);
  }
}
