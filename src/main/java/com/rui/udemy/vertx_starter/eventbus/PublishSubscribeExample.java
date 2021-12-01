package com.rui.udemy.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishSubscribeExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new SubscribeA());
    vertx.deployVerticle(SubscribeB.class.getName(),
      new DeploymentOptions().setInstances(1));
  }

  static class Publish extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000, id -> {
        vertx.eventBus().publish(Publish.class.getName(), "Hello world from Publish");
      });
    }

  }

  static class SubscribeA extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeA.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
        LOG.debug("Received message {} in {}", this.getClass(), message.body());
      });
    }
  }

  public static class SubscribeB extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeB.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
        LOG.debug("Received message {} in {}", this.getClass(), message.body());
      });
    }
  }
}
