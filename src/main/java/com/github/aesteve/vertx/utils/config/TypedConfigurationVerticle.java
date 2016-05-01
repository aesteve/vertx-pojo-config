package com.github.aesteve.vertx.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aesteve.vertx.utils.config.exceptions.InvalidConfigurationException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;

public abstract class TypedConfigurationVerticle<T> extends AbstractVerticle {

  private T config;
  private ObjectMapper mapper;
  private Validator validator;

  public abstract Class<T> getConfigClass();

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    createConfig();
  }

  public T getConfig() {
    return config;
  }

  private void createConfig() {
    if (mapper == null) {
      mapper = new ObjectMapper();
    }
    try {
      config = mapper.readValue(config().toString(), getConfigClass());
    } catch (IOException ioe) {
      throw new InvalidConfigurationException(ioe);
    }
    try {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      validator = factory.getValidator();
    } catch(NoClassDefFoundError t) { // NOSONAR
      // Swallow, bean validation is not in classpath
    }
    if (validator != null) {
      Set<ConstraintViolation<T>> violations = validator.validate(config);
      if (!violations.isEmpty()) {
        throw new InvalidConfigurationException(violations);
      }
    }
  }

}
