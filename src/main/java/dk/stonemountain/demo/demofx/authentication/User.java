package dk.stonemountain.demo.demofx.authentication;

import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private StringProperty userName = new SimpleStringProperty();
    private StringProperty userId = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private BooleanProperty loggedIn = new SimpleBooleanProperty();
    private ObjectProperty<LocalDateTime> accessTokenExpiration = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> refreshTokenExpiration = new SimpleObjectProperty<>();
    private StringProperty accessToken = new SimpleStringProperty();
    private StringProperty refreshToken = new SimpleStringProperty();

    public StringProperty getUserNameProperty() {
        return userName;
    }
    public String getUserName() {
        return userName.get();
    }
    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public StringProperty getUserIdProperty() {
        return userId;
    }
    public String getUserId() {
        return userId.get();
    }
    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public StringProperty getEmailProperty() {
        return email;
    }
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email.set(email);
    }

    public BooleanProperty getLoggedInProperty() {
        return loggedIn;
    }
    public Boolean getLoggedIn() {
        return loggedIn.get();
    }
    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn.set(loggedIn);
    }

    public ObjectProperty<LocalDateTime> getAccessTokenExpirationProperty() {
        return accessTokenExpiration;
    }
    public LocalDateTime getAccessTokenExpiration() {
        return accessTokenExpiration.get();
    }
    public void setAccessTokenExpiration(LocalDateTime accessTokenExpiration) {
        this.accessTokenExpiration.set(accessTokenExpiration);
    }

    public ObjectProperty<LocalDateTime> getRefreshTokenExpirationProperty() {
        return refreshTokenExpiration;
    }
    public LocalDateTime getRefreshTokenExpiration() {
        return refreshTokenExpiration.get();
    }
    public void setRefreshTokenExpiration(LocalDateTime refreshTokenExpiration) {
        this.refreshTokenExpiration.set(refreshTokenExpiration);
    }

    public StringProperty getAccessTokenProperty() {
        return accessToken;
    }
    public String getAccessToken() {
        return accessToken.get();
    }
    public void setAccessToken(String accessToken) {
        this.accessToken.set(accessToken);
    }
    
    public StringProperty getRefreshTokenProperty() {
        return refreshToken;
    }
    public String getRefreshToken() {
        return refreshToken.get();
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken.set(refreshToken);
    }
    @Override
    public String toString() {
        return "User [userName=" + userName + ", userId=" + userId + ", email=" + email + ", loggedIn=" + loggedIn
                + ", accessTokenExpiration=" + accessTokenExpiration + ", refreshTokenExpiration="
                + refreshTokenExpiration + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken + "]";
    }
}
