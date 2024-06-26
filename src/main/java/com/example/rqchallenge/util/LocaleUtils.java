package com.example.rqchallenge.util;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class LocaleUtils {

  private MessageSource messageSource;

  private static LocaleUtils instance;

  public LocaleUtils() {
    instance = this;
  }

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public static String getMessage(String key, Object... args) {
    return instance.messageSource.getMessage(key, args, Locale.US);
  }
}
