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

package uk.gov.hmrc.internallicensingmanagementapi.controllers

import scala.concurrent.{ExecutionContext, Future}

import org.apache.pekko.stream.Materializer
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

import uk.gov.hmrc.internallicensingmanagementapi.connectors.ILMSConnector
import uk.gov.hmrc.internallicensingmanagementapi.mocks.AuthConnectorMockModule
import uk.gov.hmrc.internallicensingmanagementapi.models.{ILMSRequest, ILMSResponse}

class ILMSControllerSpec extends HmrcSpec with GuiceOneAppPerSuite with AuthConnectorMockModule {
  implicit def mat: Materializer = app.injector.instanceOf[Materializer]

  implicit val ec = ExecutionContext.global

  private val emptyRequest: ILMSRequest = ILMSRequest(
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None,
    None
  )
  private val fakeRequest               = FakeRequest("PUT", "/customs/licence/abcdef").withHeaders("content-type" -> "application/json").withBody(emptyRequest)
  val ilmsMock                          = mock[ILMSConnector]
  private val controller                = new ILMSController(Helpers.stubControllerComponents(), ilmsMock, mockAuthConnector)

  "PUT" should {
    "return 200" in {
      Authorise.asPrivilegedApplication()

      when(ilmsMock.send(*, *)(*)).thenReturn(Future.successful((Status.OK, ILMSResponse(None, None, None, None, None, None))))
      val result = controller.licence("abcdef")(fakeRequest)
      status(result) shouldBe Status.OK
    }

    "return 401 when auth'd as a standard app" in {
      Authorise.asStandardApplication()

      when(ilmsMock.send(*, *)(*)).thenReturn(Future.successful((Status.OK, ILMSResponse(None, None, None, None, None, None))))
      val result = controller.licence("abcdef")(fakeRequest)
      status(result) shouldBe Status.FORBIDDEN
    }

    "return 503 when downstream does" in {
      Authorise.asPrivilegedApplication()

      when(ilmsMock.send(*, *)(*)).thenReturn(Future.successful((Status.INTERNAL_SERVER_ERROR, ILMSResponse(None, None, None, None, None, None))))
      val result = controller.licence("abcdef")(fakeRequest)
      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
    }
  }
}
