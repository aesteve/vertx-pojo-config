package com.github.aesteve.vertx.utils.config.tests;

import com.github.aesteve.vertx.utils.config.TypedConfigurationVerticle;
import com.github.aesteve.vertx.utils.config.exceptions.InvalidConfigurationException;
import com.github.aesteve.vertx.utils.config.tests.confs.SimpleConf;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

import java.io.IOException;

public class TestSimpleConfiguration extends TestBase {

  private static final String CONF_STRING = "Snoopy";
  private static final int CONF_INT = 42;

  class SimpleConfigurationVerticle extends TypedConfigurationVerticle<SimpleConf> {
    @Override
    public Class<SimpleConf> getConfigClass() {
      return SimpleConf.class;
    }
  }

  @Test
  public void testValidConfig(TestContext context) {
    SimpleConfigurationVerticle verticle = new SimpleConfigurationVerticle();
    vertx.deployVerticle(verticle, validOptions(), context.asyncAssertSuccess(id -> {
      SimpleConf conf = verticle.getConfig();
      context.assertNotNull(conf);
      context.assertEquals(CONF_STRING, conf.getHost());
      context.assertEquals(CONF_INT, conf.getPort());
    }));
  }

  @Test
  public void testInvalidConfig(TestContext context) {
    vertx.deployVerticle(new SimpleConfigurationVerticle(), invalidOptions(), context.asyncAssertFailure(exception -> {
      context.assertTrue(exception instanceof VertxException);
      context.assertTrue(exception instanceof InvalidConfigurationException);
      context.assertTrue(exception.getCause() instanceof IOException); // Jackson's contract is to throw IOExceptions
    }));
  }

  private DeploymentOptions validOptions() {
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put("host", CONF_STRING).put("port", CONF_INT));
    return options;
  }

  private DeploymentOptions invalidOptions() {
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put("host", "Snoopy").put("port", "Charlie"));
    return options;
  }

}
