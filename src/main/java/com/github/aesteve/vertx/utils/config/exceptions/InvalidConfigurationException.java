package com.github.aesteve.vertx.utils.config.exceptions;

import io.vertx.core.VertxException;

public class InvalidConfigurationException extends VertxException {

  public InvalidConfigurationException(Throwable cause) {
    super(cause);
  }

}
