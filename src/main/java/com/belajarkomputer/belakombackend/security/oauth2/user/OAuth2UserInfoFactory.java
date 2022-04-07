package com.belajarkomputer.belakombackend.security.oauth2.user;

import com.belajarkomputer.belakombackend.exceptions.OAuth2AuthenticationProcessingException;
import com.belajarkomputer.belakombackend.model.entity.Provider;

import java.util.Map;

public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    if(registrationId.equalsIgnoreCase(Provider.GOOGLE.toString())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
    }
  }
}
