package dk.stonemountain.demo.demofx;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.Duration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public enum Backend {
	LOCAL("Developer Workstation", "http://localhost:8180", true, false),
	TEST("Test", "https://stonemountain.dk/test", true, true),
	PRODUCTION("Production", "https://stonemountain.dk", true, true);
	
	private final String name;
	private final String bffServiceUrl;
	private final boolean enabled;
	private final boolean useJBTrust;
	
	private Backend(String name, String url, boolean enabled, boolean useJBTrust) {
		this.name = name;
		this.bffServiceUrl = url;
		this.enabled = enabled;
		this.useJBTrust = useJBTrust;
	}

	public String getName() {
		return name;
	}

	public String getBffServiceUrl() {
		return bffServiceUrl;
	}
	
	public HttpClient.Builder getHttpClientBuilder() {
		if (useJBTrust) {
			return getHttpClientBuilderWithJBTrust();
		} else {
	        return HttpClient.newBuilder()
        		.connectTimeout(Duration.ofSeconds(2))
        		.version(Version.HTTP_1_1);
	    }
	}
	
	private HttpClient.Builder getHttpClientBuilderWithJBTrust() {
        try (InputStream stream = Backend.class.getResourceAsStream("/jb.jks")) {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        char[] trustStorePassword = "testing".toCharArray();
	        trustStore.load(stream, trustStorePassword);
	        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	        factory.init(trustStore);
	        TrustManager[] managers = factory.getTrustManagers();
			
	        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
	        sslContext.init(null, managers, new SecureRandom());
	        
	        return HttpClient.newBuilder()
        		.connectTimeout(Duration.ofSeconds(2))
        		.sslContext(sslContext)
        		.version(Version.HTTP_1_1);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to setup SSL for connection", e);
	    }
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}
