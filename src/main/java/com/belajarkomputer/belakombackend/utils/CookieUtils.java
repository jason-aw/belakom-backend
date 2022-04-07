package com.belajarkomputer.belakombackend.utils;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

public class CookieUtils {
  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();

    if (cookiesExists(cookies)) {
      return Arrays.stream(cookies)
          .filter(cookie -> cookie.getName().equals(name))
          .findFirst();
//      for (Cookie cookie : cookies) {
//        if (cookie.getName().equals(name)) {
//          return Optional.of(cookie);
//        }
//      }
    }

    return Optional.empty();
  }

  public static void addCookie(HttpServletResponse response,
      String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public static void deleteCookie(HttpServletRequest request,
      HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookiesExists(cookies)) {
      Arrays.stream(cookies).forEach(cookie -> {
        if (cookie.getName().equals(name)) {
          cookie.setValue("");
          cookie.setPath("/");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        }
      });
//      for (Cookie cookie: cookies) {
//        if (cookie.getName().equals(name)) {
//          cookie.setValue("");
//          cookie.setPath("/");
//          cookie.setMaxAge(0);
//          response.addCookie(cookie);
//        }
//      }
    }
  }

  public static String serialize(Object object) {
    return Base64.getUrlEncoder()
        .encodeToString(SerializationUtils.serialize(object));
  }

  public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
    return clazz.cast(SerializationUtils.deserialize(
        Base64.getUrlDecoder().decode(cookie.getValue())));
  }

  private static boolean cookiesExists(Cookie[] cookies) {
    return Objects.nonNull(cookies) && cookies.length > 0;
  }
}
