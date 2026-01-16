/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.internallicensingmanagementapi.connectors

import scala.io.Source

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, OptionValues}
import org.scalatestplus.play.WsScalaTestClient

import play.api.http.Status.OK
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

trait BaseConnectorIntegrationSpec
    extends AnyWordSpec
    with Matchers
    with OptionValues
    with WsScalaTestClient
    with DefaultAwaitTimeout
    with FutureAwaits
    with BeforeAndAfterAll
    with BeforeAndAfterEach {
  val stubPort       = sys.env.getOrElse("WIREMOCK", "22222").toInt
  val stubHost       = "localhost"
  val wireMockUrl    = s"http://$stubHost:$stubPort"
  val wireMockServer = new WireMockServer(wireMockConfig().port(stubPort))

  override def beforeAll(): Unit = {
    super.beforeAll()
    wireMockServer.start()
    WireMock.configureFor(stubHost, stubPort)
  }

  override def afterAll(): Unit = {
    wireMockServer.stop()
    super.afterAll()
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    wireMockServer.resetMappings()
    wireMockServer.resetRequests()
  }

  override def afterEach(): Unit = {
    wireMockServer.resetMappings()
    super.afterEach()
  }

  def stubGet(path: String, status: Int = OK, filename: String = "ilms-response-valid.json") = stubFor(
    get(urlEqualTo(s"$path"))
      .willReturn(
        aResponse()
          .withStatus(status)
          .withHeader("Content-Type", "application/json")
          .withBody(Source.fromResource(s"./$filename").getLines().mkString)
      )
  )

  def stubPost(path: String, status: Int = OK, filename: String = "ilms-response-valid.json") = stubFor(
    post(urlEqualTo(s"$path"))
      .willReturn(
        aResponse()
          .withStatus(status)
          .withHeader("Content-Type", "application/json")
          .withBody(Source.fromResource(s"./$filename").getLines().mkString)
      )
  )

  def stubPut(path: String, status: Int = OK, filename: String = "ilms-response-valid.json") = stubFor(
    put(urlEqualTo(s"$path"))
      .withHeader("authorization", equalTo("bearer TESTTOKEN"))
      .willReturn(
        aResponse()
          .withStatus(status)
          .withHeader("Content-Type", "application/json")
          .withBody(Source.fromResource(s"./$filename").getLines().mkString)
      )
  )
}
