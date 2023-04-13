package dashboard.sma.adapter.webui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@Disabled
class AdapterServiceTest {

  @Autowired private SmaWebUiClient adapterService;

  @Autowired private AdapterSettings adapterSettings;

  @Test
  public void testAuth() throws JSONException {
    adapterService.authenticate();
  }

  @Test
  public void testGetValues() {
    adapterService.getValues();
  }

  @SneakyThrows
  @Test
  public void testJSONObject() {

    String jsonToCheck =
        "{\"destDev\":[],\"keys\":[\"6380_40251E00\",\"6100_40463600\",\"6400_00462400\",\"6400_00462500\",\"6100_0046C200\"]}";

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("destDev", new JSONArray());
    jsonObject.put(
        "keys",
        new JSONArray(
            List.of(
                "6380_40251E00",
                "6100_40463600",
                "6400_00462400",
                "6400_00462500",
                "6100_0046C200")));
    log.info(jsonObject.toString());

    Assertions.assertEquals(jsonToCheck, jsonObject.toString());
  }

  @Test
  public void testValuesResult() throws JSONException {

    String result =
        "{\"result\":{\"01B8-xxxxx04D\":{\"6380_40251E00\":{\"1\":[{\"val\":1060},{\"val\":827}]},\"6100_0046C200\":{\"9\":[{\"val\":1796}]},\"6100_00496900\":{\"9\":[{\"val\":1656}]},\"6100_00496A00\":{\"9\":[{\"val\":0}]},\"6100_00295A00\":{\"9\":[{\"val\":15}]},\"6100_40463700\":{\"9\":[{\"val\":21}]},\"6100_40463600\":{\"9\":[{\"val\":0}]}}}}";

    JSONObject jsonObject = new JSONObject(result);
    JSONObject resultObject = (JSONObject) jsonObject.get("result");
    Assertions.assertNotNull(resultObject);
    String fstLevel = String.valueOf(resultObject.keys().next());
    Assertions.assertTrue(resultObject.has("01B8-xxxxx04D"));
  }

  @Test
  public void testParseValues() throws JSONException {

    String result =
        "{\"result\":{\"01B8-xxxxx04D\":{\"6380_40251E00\":{\"1\":[{\"val\":1060},{\"val\":827}]},\"6100_0046C200\":{\"9\":[{\"val\":1796}]},\"6100_00496900\":{\"9\":[{\"val\":1656}]},\"6100_00496A00\":{\"9\":[{\"val\":0}]},\"6100_00295A00\":{\"9\":[{\"val\":15}]},\"6100_40463700\":{\"9\":[{\"val\":21}]},\"6100_40463600\":{\"9\":[{\"val\":0}]}}}}";
    JSONObject jsonObject = new JSONObject(result);
    Map<String, Long> m = new HashMap<>();
    adapterService.parseValues(jsonObject, m);
    Assertions.assertEquals(m.keySet().size(), 8);
  }
}
