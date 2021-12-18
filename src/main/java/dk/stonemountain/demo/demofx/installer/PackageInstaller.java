package dk.stonemountain.demo.demofx.installer;

import java.io.IOException;
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
import dk.stonemountain.demo.demofx.util.time.TimeConverter;
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
		scheduler.scheduleAtFixedRate(this::checkInstalledVersion, 10, 300, TimeUnit.SECONDS);
		log.info("Installer has started");
	}
	
	public void checkInstalledVersion() {
		Backend backend = ApplicationContainer.getInstance().getCurrentBackend();
		Downloader downloader = new Downloader(backend.getInstallationPackagesUrl());

		downloader.checkInstalledVersion().ifPresent(info -> {
			VersionInformation swInfo = map(info);
			Platform.runLater(() -> ApplicationContainer.getInstance().updateVersion(swInfo));
				
			if (info.mustBeUpdated && (versionDownloaded.isEmpty() || !versionDownloaded.get().equalsIgnoreCase(info.recommendedVersion))) {
				downloader.getNewVersion(info.recommendedSha).ifPresent(sw -> {
					try(InputStream is = sw) {
						Path file = Files.createTempFile("demofx-", "");
						Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
						log.debug("File {} ready for installation", file);
						versionDownloaded = Optional.of(info.recommendedVersion);
						Platform.runLater(() -> ApplicationContainer.getInstance().updatedVersionReady(file));
					} catch (IOException e) {
						log.error("Failed to retrieve new sw version", e);
					}
				});
			}
		});
	}

	private VersionInformation map(dk.stonemountain.demo.demofx.installer.backend.VersionInformation info) {
		VersionInformation v = new VersionInformation();
		v.setMustBeUpdated(info.currentIsWorking);
		v.setNewSha(info.recommendedSha);
		v.setNewVersion(info.recommendedVersion);
		v.setNewerVersionAvailable(info.mustBeUpdated);
		v.setNewReleaseNote(info.recommendedReleaseNote);
		v.setNewReleaseTime(TimeConverter.toLocalDateTime(info.recommendedReleaseTime));
		return v;
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

	public void install(Path path) {
		// find this running instance location
		// cp this instance to demofx_old
		// mv newVersion to demofx
		// restarts
	}
}
