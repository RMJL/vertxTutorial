package com.rui.udemy.vertx_starter.eventbus.customcodec;

import io.vertx.core.*;
import io.vertx.core.eventbus.MessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {
  private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), logOnError());
    vertx.deployVerticle(new PongVerticle(), logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) {
        LOG.error("err", ar.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);

    private static final String ADDRESS = PingVerticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      Ping message = new Ping("Hello World", true);
      LOG.debug("Sending: {}", message);
      vertx.eventBus().registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      vertx.eventBus().<Pong>request(ADDRESS, message, reply -> {
        if (reply.failed()) {
          LOG.error("Failed: ", reply.cause());
          return;
        }
        LOG.debug("Response: {}", reply.result().body());
      });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      vertx.eventBus().<Ping>consumer(PingVerticle.ADDRESS, message -> {
        LOG.debug("Message received: {}", message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error -> {
        LOG.error("Error: ", error);
      });
      startPromise.complete();
    }
  }
}
