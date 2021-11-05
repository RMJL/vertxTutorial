package com.rui.udemy.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleAA extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleAA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Started {}", getClass().getName());
    startPromise.complete();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    LOG.debug("Stopped {}", getClass().getName());
    stopPromise.complete();
  }
}
