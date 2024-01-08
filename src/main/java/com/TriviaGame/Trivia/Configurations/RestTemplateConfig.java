package com.TriviaGame.Trivia.Configurations;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ssl.JksSslBundleProperties.Store;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;
import java.io.File;
import java.io.IOException; 
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;

@Configuration
public class RestTemplateConfig {
	@Value("${trust.store}")
    private String trustStore;
	@Value("${trust.store.password}")
	private String trustStorePassword;
	
	public RestTemplateConfig() {};
	
	RestTemplate restTemplate() throws Exception {
		
		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(new File(trustStore), trustStorePassword.toCharArray())
                .build();
	    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
	    factory.setHttpClient(client);
	    return new RestTemplate(factory);
	}
	
	PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
		      .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
		              .setSslContext(SSLContexts.createSystemDefault())
		              .setTlsVersions(TLS.V_1_3)
		              .build())
		      .setDefaultSocketConfig(SocketConfig.custom()
		              .setSoTimeout(Timeout.ofMinutes(1))
		              .build())
		      .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
		      .setConnPoolPolicy(PoolReusePolicy.LIFO)
		      .setDefaultConnectionConfig(ConnectionConfig.custom()
		              .setSocketTimeout(Timeout.ofMinutes(1))
		              .setConnectTimeout(Timeout.ofMinutes(1))
		              .setTimeToLive(TimeValue.ofMinutes(10))
		              .build())
		      .build();
	
	CloseableHttpClient client = HttpClients.custom()
		      .setConnectionManager(connectionManager)
		      .setDefaultRequestConfig(RequestConfig.custom()
		              .setCookieSpec(StandardCookieSpec.STRICT)
		              .build())
		      .build();

}