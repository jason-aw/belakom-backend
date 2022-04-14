package com.belajarkomputer.belakombackend.security.oauth2;

import com.belajarkomputer.belakombackend.exceptions.OAuth2AuthenticationProcessingException;
import com.belajarkomputer.belakombackend.model.entity.Provider;
import com.belajarkomputer.belakombackend.model.entity.Role;
import com.belajarkomputer.belakombackend.model.entity.User;
import com.belajarkomputer.belakombackend.repository.UserRepository;
import com.belajarkomputer.belakombackend.security.UserPrincipal;
import com.belajarkomputer.belakombackend.security.oauth2.user.OAuth2UserInfo;
import com.belajarkomputer.belakombackend.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;


@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws
      OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
    if(!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }

    User user = this.userRepository.findByEmail(oAuth2UserInfo.getEmail());
    if (Objects.nonNull(user)) {
      if(!user.getProvider().equals(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))) {
        throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
            user.getProvider() + " account. Please use your " + user.getProvider() +
            " account to login.");
      }
      user = this.updateExistingUser(user, oAuth2UserInfo);
    } else {
      user = this.registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
    }

    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user = new User();

    user.setProvider(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
    user.setProviderId(oAuth2UserInfo.getId());
    user.setName(oAuth2UserInfo.getName());
    user.setEmail(oAuth2UserInfo.getEmail());
    user.setImageUrl(oAuth2UserInfo.getImageUrl());
    user.setRole(Role.USER);
    return this.userRepository.save(user);
  }

  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setName(oAuth2UserInfo.getName());
    existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
    return this.userRepository.save(existingUser);
  }

}