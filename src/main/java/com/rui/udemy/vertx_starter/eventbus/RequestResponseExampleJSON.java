package com.rui.udemy.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExampleJSON {
  private static final Logger LOG = LoggerFactory.getLogger(RequestResponseExampleJSON.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticleA());
//    vertx.deployVerticle(new ResponseVerticleB());
  }

  static class RequestVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

    private static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      String message = "Hello World!";
      LOG.debug("Sending: {}", message);
      vertx.eventBus().<String>request(ADDRESS, message, reply -> {
        LOG.debug("Response: {}", reply.result().body());
      });
    }
  }

  static class ResponseVerticleA extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticleA.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message -> {
          LOG.debug("Message received: {}", message.body());
          message.reply("Received your message. Thanks.");
        });
    }
  }

  static class ResponseVerticleB extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticleB.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message -> {
          LOG.debug("Message received: {}", message.body());
          message.reply("Received your message. Thanks.");
        });
    }
  }
}
