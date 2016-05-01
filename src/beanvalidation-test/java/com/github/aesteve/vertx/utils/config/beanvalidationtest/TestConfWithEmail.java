package com.github.aesteve.vertx.utils.config.beanvalidationtest;

import com.github.aesteve.vertx.utils.config.TypedConfigurationVerticle;
import com.github.aesteve.vertx.utils.config.beanvalidationtest.confs.ValidatedConf;
import com.github.aesteve.vertx.utils.config.exceptions.InvalidConfigurationException;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

import java.io.IOException;

public class TestConfWithEmail extends TestBase {

  private static final String VALID_MAIL = "snoopy@peanuts.com";
  private static final String INVALID_MAIL = "lucy_from_peanuts.com";

  class EmailConfVerticle extends TypedConfigurationVerticle<ValidatedConf> {
    @Override
    public Class<ValidatedConf> getConfigClass() {
      return ValidatedConf.class;
    }
  }

  @Test
  public void testValidConfig(TestContext context) {
    EmailConfVerticle verticle = new EmailConfVerticle();
    vertx.deployVerticle(verticle, validOptions(), context.asyncAssertSuccess(id -> {
      ValidatedConf conf = verticle.getConfig();
      context.assertNotNull(conf);
      context.assertEquals(VALID_MAIL, conf.getEmail());
    }));
  }

  @Test
  public void testInvalidConfig(TestContext context) {
    vertx.deployVerticle(new EmailConfVerticle(), invalidOptions(), context.asyncAssertFailure(exception -> {
      context.assertTrue(exception instanceof VertxException);
      context.assertTrue(exception instanceof InvalidConfigurationException);
      InvalidConfigurationException ice = (InvalidConfigurationException)exception;
      System.out.println(ice.getMessage());
    }));
  }

  private DeploymentOptions validOptions() {
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put("email", VALID_MAIL));
    return options;
  }

  private DeploymentOptions invalidOptions() {
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put("email", INVALID_MAIL));
    return options;
  }

}
