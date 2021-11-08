package com.rui.udemy.vertx_starter.eventloops;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExampleTest extends AbstractVerticle {

private static final Logger LOG = LoggerFactory.getLogger(EventLoopExampleTest.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new EventLoopExampleTest());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // on the event loop thread
    System.out.println("Calling from " + Thread.currentThread().getName());

    Handler<Promise<Integer>> blockingCodeHandler = promise -> {
      // executed on a worker thread
      System.out.println("Work executed on " + Thread.currentThread().getName());
      promise.complete(3);
    };

    Handler<AsyncResult<Integer>> resultHandler = result -> {
      // back on the calling event loop thread
      System.out.println("Result '" + result.result() + "' received on " + Thread.currentThread().getName());
    };

    vertx.executeBlocking(blockingCodeHandler, resultHandler);
  }
}
