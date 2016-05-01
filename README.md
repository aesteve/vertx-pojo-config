## Vertx-typesafe-config

A very simple tool to map Vert.x's json configuration onto a typed configuration Java bean.

It uses Jackson's object mapper behind the hood.


### How to use it ?

Simply make your `Verticle` extend `TypedConfigurationVerticle` and declare the class of your configuration :

```java
class SimpleConf {

  private String host;
  private int port;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
  
}
```

```java
class SimpleConfigurationVerticle extends TypedConfigurationVerticle<SimpleConf> {

    @Override
    public Class<SimpleConf> getConfigClass() {
      return SimpleConf.class;
    }
    
    
}

```

Then, from within your verticle just call `getConfig()` to get the `SimpleConf` instance. The `config()` method will still be returning the `JsonObject` configuration.