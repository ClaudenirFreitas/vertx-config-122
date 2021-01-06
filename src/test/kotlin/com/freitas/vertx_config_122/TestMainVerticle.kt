package com.freitas.vertx_config_122

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.junit5.web.TestRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.jsonArrayOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*


@ExtendWith(VertxExtension::class)
class TestMainVerticle {

  @BeforeEach
  fun deploy_verticle(vertx: Vertx, testContext: VertxTestContext) {
    vertx.deployVerticle(MainVerticle(), testContext.succeeding { testContext.completeNow() })
  }

  @Test
  fun verticle_deployed(vertx: Vertx, testContext: VertxTestContext) {
    val client = vertx.createHttpClient()
    client.request(HttpMethod.GET, 8888, "localhost", "/")
      .compose { req -> req.send().compose(HttpClientResponse::body) }
      .onComplete(testContext.succeeding {
        testContext.verify {
          val response = it.toJsonObject()

          val server = response.getJsonObject("server")
          assertEquals("localhost", server.getString("host"))
          assertEquals(8888, server.getInteger("port"))

          val multiple = response.getJsonObject("multiple")
          assertEquals(jsonArrayOf(1,2,3), multiple.getJsonArray("values"))

          testContext.completeNow()
        }
      })
  }

}
