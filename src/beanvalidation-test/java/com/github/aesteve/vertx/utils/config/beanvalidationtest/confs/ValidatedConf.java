package com.github.aesteve.vertx.utils.config.beanvalidationtest.confs;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

public class ValidatedConf {

  @NotNull
  @Email
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
