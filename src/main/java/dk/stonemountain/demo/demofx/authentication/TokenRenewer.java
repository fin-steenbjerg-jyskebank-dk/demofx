package dk.stonemountain.demo.demofx.authentication;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.ApplicationContainer;

public class TokenRenewer {
	private static final Logger log = LoggerFactory.getLogger(TokenRenewer.class);

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
		Thread t = Executors.defaultThreadFactory().newThread(r);
		t.setDaemon(true);
		return t;
	});

	public void startReneval() {
		scheduler.scheduleAtFixedRate(this::checkAndRenew, 10, 60, TimeUnit.SECONDS);
		log.info("Token renewer has started");
	}

	public void checkAndRenew() {
		var user = ApplicationContainer.getInstance().getUser();
		log.debug("Checking tokens for user : {}", user);
		if (user.getLoggedIn().booleanValue()) {
			if (user.getAccessTokenExpiration().isBefore(LocalDateTime.now().plus(45, ChronoUnit.SECONDS))) {
				if (user.getRefreshTokenExpiration().isAfter(LocalDateTime.now())) {
					try {
						log.debug("Renewing token");
						new OauthServer().getTokenFromRefresh(AuthenticationManager.CLIENT, user.getRefreshToken()).ifPresent(t -> {
							user.setAccessToken(t.token());
							user.setAccessTokenExpiration(LocalDateTime.now().plusSeconds(t.expiresIn()));
							user.setRefreshToken(t.refreshToken());
							user.setRefreshTokenExpiration(LocalDateTime.now().plusSeconds(t.refreshExpiresIn()));
						});
					} catch (Exception e) {
						log.error("Failed to renew access token", e);
					}
				} else {
					log.info("User being logged out due to old refresh token: {}", user.getRefreshTokenExpiration());
					user.setLoggedIn(false);
				}
			}
		}	
	}
}
