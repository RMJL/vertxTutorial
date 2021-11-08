package com.rui.udemy.vertx_starter.eventloops;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLoopExampleTestWithEventBus extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(EventLoopExampleTestWithEventBus.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new AbstractVerticle() {

      @Override
      public void start(Promise<Void> startFuture) {

        // work handler
        Handler<Message<String>> handler = message -> {
          System.out.println("Received message on " + Thread.currentThread().getName());

          // do work
          System.out.println("Working ...");

          message.reply("OK");
        };

        // wait for work
        vertx.eventBus().consumer("worker", handler).completionHandler(r -> {
          startFuture.complete();
        });
      }
    }, new DeploymentOptions().setWorker(true));

    vertx.deployVerticle(new AbstractVerticle() {
      @Override
      public void start() {

        // reply handler
        Handler<AsyncResult<Message<String>>> replyHandler = message -> {
          System.out.println(
            "Received reply '" + message.result().body() + "' on " + Thread.currentThread().getName());
        };

        // dispatch work
        vertx.eventBus().send("worker", replyHandler);
      }
    });
  }
}
