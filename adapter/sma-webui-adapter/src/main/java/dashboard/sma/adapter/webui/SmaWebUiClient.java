package dashboard.sma.adapter.webui;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class SmaWebUiClient {

  private final RestTemplate restTemplate;

  private final AdapterSettings adapterSettings;

  private String sid;

  public SmaWebUiClient(RestTemplate restTemplate, AdapterSettings adapterSettings) {
    this.restTemplate = restTemplate;
    this.adapterSettings = adapterSettings;
  }

  protected void authenticate() throws SmaWebUiClientException {
    try {
      log.info("Logging in to SMA Web UI");
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("right", this.adapterSettings.getRole());
      jsonObject.put("pass", this.adapterSettings.getPasswd());

      var res =
          restTemplate.postForEntity(
              this.adapterSettings.getEndpoints().get(AdapterSettings.ENDPOINT_LOGIN),
              jsonObject.toString(),
              JsonNode.class);
      if (res.getStatusCode() == HttpStatus.OK) {
        var value = Objects.requireNonNull(res.getBody()).findValue("sid");
        sid = value.asText();
        log.info("Got session id {}", sid);
      } else {
        sid = null;
        throw new SmaWebUiClientException(
            String.format(
                "Unable to authenticate. Response status code = %s", res.getStatusCode()));
      }
    } catch (Exception e) {
      throw new SmaWebUiClientException(e);
    }
  }

  public void logout() {
    if (this.sid != null) {
      log.info("Logging out from SMA Web UI");
      UriComponentsBuilder uriComponentsBuilder =
          UriComponentsBuilder.fromHttpUrl(
              this.adapterSettings.getEndpoints().get(AdapterSettings.ENDPOINT_LOGOUT));
      uriComponentsBuilder.queryParam("sid", this.sid);
      log.debug(uriComponentsBuilder.build().toUriString());
      var res =
          restTemplate.postForEntity(
              uriComponentsBuilder.build().toUriString(), "{}", JsonNode.class);
      this.sid = null;
    }
  }

  protected Map<String, Long> getValues() {

    try {
      if (this.sid == null) {
        authenticate();
      }

      JSONObject requestBody = new JSONObject();
      requestBody.put("destDev", new JSONArray());
      requestBody.put("keys", new JSONArray(this.adapterSettings.getValues().keySet()));

      UriComponentsBuilder uriComponentsBuilder =
          UriComponentsBuilder.fromHttpUrl(
              this.adapterSettings.getEndpoints().get(AdapterSettings.ENDPOINT_VALUES));
      uriComponentsBuilder.queryParam("sid", this.sid);
      log.debug(uriComponentsBuilder.build().toUriString());
      log.debug(requestBody.toString());
      var res =
          restTemplate.postForEntity(
              uriComponentsBuilder.build().toUriString(), requestBody.toString(), JsonNode.class);

      if (res.getStatusCode() == HttpStatus.OK) {
        JSONObject result = new JSONObject(res.getBody().toString());
        HashMap<String, Long> measures = new HashMap<>();
        parseValues(result, measures);
        return measures;
      } else {
        throw new SmaWebUiClientException(
            String.format("Unable to get values. Response status code = %s", res.getStatusCode()));
      }

    } catch (Exception e) {
      throw new SmaWebUiClientException(e);
    }
  }

  protected void parseValues(JSONObject jsonObject, Map<String, Long> map) throws JSONException {

    Iterator it = jsonObject.keys();
    while (it.hasNext()) {
      String key = String.valueOf(it.next());
      if (adapterSettings.getValues().containsKey(key)) {
        JSONObject valuesNode = jsonObject.optJSONObject(key);
        if (valuesNode != null) {
          JSONArray array = valuesNode.optJSONArray(String.valueOf(valuesNode.names().get(0)));
          if (array != null) {
            if (array.length() == 1) {
              map.put(adapterSettings.getValues().get(key), array.getJSONObject(0).getLong("val"));
            } else {
              for (int i = 0; i < array.length(); i++) {
                map.put(
                    adapterSettings.getValues().get(key) + "_" + (i + 1),
                    array.getJSONObject(i).getLong("val"));
              }
            }
          }
        }
      } else {
        Object next = jsonObject.opt(key);
        if (next instanceof JSONObject jo) {
          parseValues(jo, map);
        }
      }
    }
  }
}
