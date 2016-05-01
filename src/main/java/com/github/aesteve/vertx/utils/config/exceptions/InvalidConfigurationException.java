package com.github.aesteve.vertx.utils.config.exceptions;

import io.vertx.core.VertxException;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

public class InvalidConfigurationException extends VertxException {

  final transient ConstraintValidationMsg msg = new ConstraintValidationMsg();

  public InvalidConfigurationException(Throwable cause) {
    super(cause);
  }

  public<T> InvalidConfigurationException(Set<ConstraintViolation<T>> violations) {
    super("Configuration validation failed. \n");
    if (violations != null) {
      violations.forEach(msg::append);
    }
  }

  @Override
  public String getMessage() {
    String message = super.getMessage();
    if (!msg.isEmpty()) {
      message += msg.toString();
    }
    return message;
  }

  private class ConstraintValidationMsg<T> {

    private StringBuilder sb;

    ConstraintValidationMsg() {
      sb = new StringBuilder();
    }

    @Override
    public String toString() {
      return sb.toString();
    }

    boolean isEmpty() {
      return sb.length() == 0;
    }

    void append(ConstraintViolation<T> constraintViolation) {
      sb.append("\tConstraint violation on field ")
          .append(constraintViolation.getPropertyPath())
          .append(" with value ")
          .append(constraintViolation.getInvalidValue())
          .append(". Error is: ")
          .append(constraintViolation.getMessage()).append(". ");
    }

  }

}
