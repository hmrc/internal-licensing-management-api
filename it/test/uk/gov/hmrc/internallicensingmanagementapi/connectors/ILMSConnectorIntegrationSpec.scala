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

import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import play.api.http.Status
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration, Mode}
import uk.gov.hmrc.http.HeaderCarrier

import uk.gov.hmrc.internallicensingmanagementapi.models.{ILMSRequest, ILMSResponse}

class ILMSConnectorIntegrationSpec extends BaseConnectorIntegrationSpec with GuiceOneAppPerSuite {

  private val stubConfig = Configuration(
    "microservice.services.internal-licensing-management.port" -> stubPort
  )

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .configure(stubConfig)
      .in(Mode.Test)
      .build()

  private val emptyResponse             = ILMSResponse(None, None, None, None, None, None)
  private val emptyRequest: ILMSRequest = ILMSRequest(None, None, None, None, None, None, None, None, None, None, None)

  implicit val hc: HeaderCarrier = HeaderCarrier()
  val underTest                  = app.injector.instanceOf[ILMSConnector]

  "send" should {
    "pass back a success" in {
      stubPut("/customs/licence/abc")
      val result = await(underTest.send("abc", emptyRequest))
      result._1 shouldBe Status.OK
      result._2 shouldBe emptyResponse
    }

    "pass back an error" in {
      stubPut("/customs/licence/abc", Status.BAD_REQUEST)
      val result = await(underTest.send("abc", emptyRequest))
      result._1 shouldBe Status.BAD_REQUEST
      result._2 shouldBe emptyResponse
    }

  }
}
