package com.freitas.vertx_config_122

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject

import io.vertx.config.ConfigStoreOptions
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigRetriever


class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {

    val propertyWithRawData = ConfigStoreOptions()
      .setFormat("properties")
      .setType("file")
      .setConfig(
        JsonObject()
          .put("path", "conf/raw.properties")
          .put("raw-data", true)
          .put("hierarchical", true)
      )

    val retriever = ConfigRetriever.create(vertx, ConfigRetrieverOptions().addStore(propertyWithRawData))

    vertx
      .createHttpServer()
      .requestHandler {

        retriever.getConfig { config ->
          if (config.failed()) {
            it.response()
              .putHeader("content-type", "text/plain")
              .end("Error. Detail: ${config.cause()}")
          } else {
            it.response()
              .putHeader("content-type", "application/json")
              .end(config.result().encode())
          }
        }

      }
      .listen(8888) {
        if (it.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(it.cause());
        }
      }
  }

}
