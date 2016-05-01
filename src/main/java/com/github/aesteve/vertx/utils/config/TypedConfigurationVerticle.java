package com.github.aesteve.vertx.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aesteve.vertx.utils.config.exceptions.InvalidConfigurationException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

import java.io.IOException;

public abstract class TypedConfigurationVerticle<T> extends AbstractVerticle {

  private T config;
  private ObjectMapper mapper;

  public abstract Class<T> getConfigClass();

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    createConfig();
  }

  public T getConfig() {
    return config;
  }

  private void createConfig() throws InvalidConfigurationException {
    if (mapper == null) {
      mapper = new ObjectMapper();
    }
    try {
      config = mapper.readValue(config().toString(), getConfigClass());
    } catch (IOException ioe) {
      throw new InvalidConfigurationException(ioe);
    }
  }

}
