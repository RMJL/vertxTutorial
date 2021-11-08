package com.rui.udemy.vertx_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(WorkerExample.class.getName(),new DeploymentOptions()
      .setWorker(true)
      .setWorkerPoolSize(2)
      .setWorkerPoolName("my-worker-verticle-parent")
      .setInstances(2));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.deployVerticle(WorkerVerticle.class.getName(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(2)
        .setWorkerPoolName("my-worker-verticle-child")
      );

    executeBlockingCode();
    startPromise.complete();
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
      LOG.debug("Executing blocking code.");
      try {
        Thread.sleep(5000);
        event.complete();
      } catch (InterruptedException e) {
        LOG.error("Failed : ", e);
        event.fail(e);
      }
    }, result -> {
      if (result.succeeded()) {
        LOG.debug("Blocking code done.");
      } else {
        LOG.debug("Blocking code failed due to :", result.cause());
      }
    });
  }
}
