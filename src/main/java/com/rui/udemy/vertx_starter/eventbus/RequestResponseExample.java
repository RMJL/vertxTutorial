package com.rui.udemy.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {
  private static final Logger LOG = LoggerFactory.getLogger(RequestResponseExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
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

  static class ResponseVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

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
