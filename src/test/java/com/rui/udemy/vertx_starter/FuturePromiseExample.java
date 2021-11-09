package com.rui.udemy.vertx_starter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {
  private static final Logger LOG = LoggerFactory.getLogger(FuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start");

    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Promise was a success");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext context) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start");

    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed"));
      LOG.debug("Promise was a failure");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext context) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start");

    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Timer done.");
    });

    Future<String> future = promise.future();
    future
      .onSuccess(result -> {
        LOG.debug("Result: {}", result);
        context.completeNow();
      })
      .onFailure(context::failNow);
  }

  @Test
  void future_failure(Vertx vertx, VertxTestContext context) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start");

    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Promise failed"));
      LOG.debug("Timer done.");
    });

    Future<String> future = promise.future();
    future
      .onSuccess(context::failNow)
      .onFailure(error -> {
        LOG.debug("Result: {}", error);
        context.completeNow();
      });
  }

  @Test
  void future_map(Vertx vertx, VertxTestContext context) {
    Promise<String> promise = Promise.promise();
    LOG.debug("Start");

    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Timer done.");
    });

    Future<String> future = promise.future();
    future
      .onSuccess(result -> {
        LOG.debug("Result: {}", result);
        context.completeNow();
      })
      .onFailure(context::failNow);
  }
}
