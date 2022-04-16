package com.belajarkomputer.belakombackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private final Auth auth = new Auth();
  private final OAuth2 oauth2 = new OAuth2();

  public static class Auth {
    private String tokenSecret;
    private long accessTokenExpirationSec;
    private long refreshTokenExpirationSec;
    private long googleAccessTokenExpirationSec;

    public long getGoogleAccessTokenExpirationSec() {
      return googleAccessTokenExpirationSec;
    }

    public void setGoogleAccessTokenExpirationSec(long googleAccessTokenExpirationSec) {
      this.googleAccessTokenExpirationSec = googleAccessTokenExpirationSec;
    }

    public long getRefreshTokenExpirationSec() {
      return refreshTokenExpirationSec;
    }

    public void setRefreshTokenExpirationSec(long refreshTokenExpirationSec) {
      this.refreshTokenExpirationSec = refreshTokenExpirationSec;
    }

    public String getTokenSecret() {
      return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
      this.tokenSecret = tokenSecret;
    }

    public long getAccessTokenExpirationSec() {
      return accessTokenExpirationSec;
    }

    public void setAccessTokenExpirationSec(long accessTokenExpirationSec) {
      this.accessTokenExpirationSec = accessTokenExpirationSec;
    }
  }

  public static final class OAuth2 {
    private List<String> authorizedRedirectUris = new ArrayList<>();

    public List<String> getAuthorizedRedirectUris() {
      return authorizedRedirectUris;
    }

    public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
      this.authorizedRedirectUris = authorizedRedirectUris;
      return this;
    }
  }

  public Auth getAuth() {
    return auth;
  }

  public OAuth2 getOauth2() {
    return oauth2;
  }
}
