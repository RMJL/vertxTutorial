package com.rui.udemy.vertx_starter;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
      .map(result -> {
        LOG.debug("Map String to object");
        return new JsonObject().put("key", result);
      })
      .map(entries -> {
        return new JsonArray().add(entries);
      })
      .onSuccess(result -> {
        LOG.debug("Result: {} of type {}", result, result.getClass().getSimpleName());
        context.completeNow();
      })
      .onFailure(context::failNow);
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer()
      .requestHandler(request -> LOG.debug("{}", request))
      .listen(10_000)
      .compose(server -> {
        LOG.debug("Another task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        LOG.debug("even more");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(server -> {
        LOG.debug("Server started on port {}", server.actualPort());
        context.completeNow();
      });
  }

  @Test
  void future_composition(Vertx vertx, VertxTestContext context) {
    Promise<Object> one = Promise.promise();
    Promise<Object> two = Promise.promise();
    Promise<Object> three = Promise.promise();

    Future<Object> futureOne = one.future();
    Future<Object> futureTwo = two.future();
    Future<Object> futureThree = three.future();

    CompositeFuture.all(futureOne, futureTwo, futureThree)
      .onFailure(context::failNow)
      .onSuccess(ar -> LOG.debug("Success"));

    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.complete();
    });

  }
}
