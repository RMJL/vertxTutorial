package com.rui.udemy.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new ReceiverA());
    vertx.deployVerticle(new ReceiverB());
  }

  static class Sender extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000, id -> {
        vertx.eventBus().send(Sender.class.getName(), "Hello World! from Sender");
      });
    }
  }

  static class ReceiverA extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ReceiverA.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Sender.class.getName(), message -> {
        LOG.debug("Message received: {}", message.body());
      });
    }
  }

  static class ReceiverB extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ReceiverB.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Sender.class.getName(), message -> {
        LOG.debug("Message received: {}", message.body());
      });
    }
  }
}
