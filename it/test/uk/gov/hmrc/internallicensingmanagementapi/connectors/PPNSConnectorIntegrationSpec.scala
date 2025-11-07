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

import java.util.UUID

import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import play.api.http.Status.NOT_FOUND
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsObject
import play.api.{Application, Configuration, Mode}
import uk.gov.hmrc.apiplatform.modules.common.domain.models.ClientId
import uk.gov.hmrc.http.{HeaderCarrier, UpstreamErrorResponse}

import uk.gov.hmrc.internallicensingmanagementapi.models._

class PPNSConnectorIntegrationSpec extends BaseConnectorIntegrationSpec with GuiceOneAppPerSuite with TestData {

  private val stubConfig = Configuration(
    "microservice.services.push-pull-notifications-api.port" -> stubPort
  )

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .configure(stubConfig)
      .in(Mode.Test)
      .build()

  implicit val hc: HeaderCarrier = HeaderCarrier()
  val underTest                  = app.injector.instanceOf[PPNSConnector]

  private val expectedUUID = UUID.fromString("956041dc-dd68-4737-8fd0-5cd58aa67297")
  private val boxId        = BoxId(expectedUUID)

  "getBoxId" should {
    val clientId    = ClientId.random
    val expectedBox = Box(boxId)

    "pass back a success" in {
      stubGet(s"/box?boxName=customs/licence%23%231.0%23%23callbackUrl&clientId=$clientId", filename = "ppns-box-valid.json")
      val result = await(underTest.getBoxId(clientId))
      result.value shouldBe expectedBox
    }

    "pass back a none when no box" in {
      stubGet(s"/box?boxName=customs/licence%23%231.0%23%23callbackUrl&clientId=$clientId", status = NOT_FOUND, filename = "ppns-box-valid.json")
      val result = await(underTest.getBoxId(clientId))
      result shouldBe None
    }

  }

  "postMessage" should {
    "pass back a success" in {
      stubPost(s"/box/$boxId/notifications", filename = "ppns-notification-valid.json")
      val result = await(underTest.postMessage(boxId, JsObject(Seq.empty)))
      result shouldBe CreateNotificationResponse(NotificationId(expectedUUID))
    }

    "pass back a not-found" in {
      stubPost(s"/box/$boxId/notifications", status = NOT_FOUND, filename = "ppns-notification-valid.json")
      intercept[UpstreamErrorResponse](await(underTest.postMessage(boxId, JsObject(Seq.empty))))
    }

  }
}
