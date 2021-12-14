package dk.stonemountain.demo.demofx.installer;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.ApplicationContainer;
import dk.stonemountain.demo.demofx.Backend;
import dk.stonemountain.demo.demofx.installer.backend.VersionInformation;
import dk.stonemountain.demo.demofx.util.jaxrs.JsonbHelper;
import javafx.application.Platform;

public class PackageInstaller {
	private static final Logger log = LoggerFactory.getLogger(PackageInstaller.class);

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
		Thread t = Executors.defaultThreadFactory().newThread(r);
		t.setDaemon(true);
		return t;
	});
	private Optional<String> versionDownloaded = Optional.empty();

  	public void startInstaller() {
		scheduler.scheduleAtFixedRate(this::checkInstalledVersion, 30, 300, TimeUnit.SECONDS);
		log.info("Installer has started");
	}
	
	public void checkInstalledVersion() {
		Backend backend = ApplicationContainer.getInstance().getCurrentBackend();

		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(backend.getBffServiceUrl()))
                .GET()
                .build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			var statusCode = response.statusCode();
			if (statusCode == 200) {
				var body = response.body();
				VersionInformation info = JsonbHelper.fromJson(body, VersionInformation.class);
				log.trace("Installer has fetch version nummer: {}", info);
				Platform.runLater(() -> ApplicationContainer.getInstance().updateVersion(info));
				
				if (info.mustBeUpdated && (versionDownloaded.isEmpty() || !versionDownloaded.get().equalsIgnoreCase(info.recommendedVersion))) {
					try(InputStream is = getNewVersion(info.recommendedSha)) {
						Path file = Files.createTempFile("demofx-" + info.recommendedSha + "-", "");
						Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
					}
					versionDownloaded = Optional.of(info.recommendedVersion);
					Platform.runLater(() -> ApplicationContainer.getInstance().updatedVersionReady(info));
				}
			} else {
				log.error("Failed to get version. Status code {}", statusCode);
			}
		} catch (Exception e) {
			log.error("Version check failed", e);
		}
	}

	public InputStream getNewVersion(String sha) {
		Backend backend = ApplicationContainer.getInstance().getCurrentBackend();

		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(backend.getBffServiceUrl()))
                .GET()
                .build();

		try {
			HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
			var statusCode = response.statusCode();
			if (statusCode == 200) {
				return response.body();
			} else {
				throw new RuntimeException("Failed to get version. Status code " + statusCode);
			}
		} catch (Exception e) {
			throw new RuntimeException("Communication failure", e);
		}
	}

}
