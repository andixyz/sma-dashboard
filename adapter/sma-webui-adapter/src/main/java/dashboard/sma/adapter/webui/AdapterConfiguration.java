package dashboard.sma.adapter.webui;

import dashboard.sma.adapter.pa.SmaDashboardPersistence;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = SmaDashboardPersistence.class)
public class AdapterConfiguration {

  @Bean
  public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyManagementException {
    return new RestTemplate(httpComponentsClientHttpRequestFactory());
  }

  public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory()
      throws NoSuchAlgorithmException, KeyManagementException {
    TrustManager[] unquestioningTrustManager =
        new TrustManager[] {
          new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
          }
        };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, unquestioningTrustManager, null);

    SSLConnectionSocketFactory sslConnectionSocketFactory =
        SSLConnectionSocketFactoryBuilder.create()
            .setSslContext(sc)
            .setHostnameVerifier((hostname, session) -> true)
            .build();

    HttpClientConnectionManager cm =
        PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .build();

    return new HttpComponentsClientHttpRequestFactory(
        HttpClients.custom().setConnectionManager(cm).evictExpiredConnections().build());
  }
}
