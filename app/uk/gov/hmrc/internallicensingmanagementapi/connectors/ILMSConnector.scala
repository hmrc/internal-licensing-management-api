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

import java.time.format.DateTimeFormatter
import java.time.{Clock, ZoneOffset}
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

import com.fasterxml.jackson.core.JacksonException

import play.api.Logger
import play.api.http.HeaderNames
import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.libs.json.{JsSuccess, Json}
import uk.gov.hmrc.apiplatform.modules.common.services.ClockNow
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, StringContextOps}

import uk.gov.hmrc.internallicensingmanagementapi.models.{ILMSRequest, ILMSResponse}

@Singleton
class ILMSConnector @Inject() (http: HttpClientV2, config: ILMSConfig, val clock: Clock)(implicit ec: ExecutionContext) extends ClockNow {
  val logger = Logger("application")

  def send(request: ILMSRequest)(implicit hc: HeaderCarrier): Future[(Int, ILMSResponse)] = {
    http.put(url"${config.baseUrl}/cds/lic01/v1")
      .setHeader(
        HeaderNames.AUTHORIZATION    -> s"Bearer ${config.bearerToken}",
        HeaderNames.DATE             -> s"${DateTimeFormatter.RFC_1123_DATE_TIME.format(instant().atOffset(ZoneOffset.UTC))}",
        HeaderNames.X_FORWARDED_HOST -> "MDTP"
      )
      .withBody(Json.toJson(request))
      .execute[HttpResponse]
      .map(resp =>
        try {
          Json.fromJson[ILMSResponse](resp.json) match {
            case JsSuccess(value, _) if value != ILMSResponse.blankResponse => (resp.status, value)
            case _                                                          =>
              logger.error(s"${resp.status} : Unexpected Json Response : ${resp.json}")
              (INTERNAL_SERVER_ERROR, ILMSResponse.internalErrorResponse)
          }
        } catch {
          case _: JacksonException =>
            logger.error(s"${resp.status} : ${resp.body}")
            (INTERNAL_SERVER_ERROR, ILMSResponse.internalErrorResponse)
        }
      )
  }
}

case class ILMSConfig(baseUrl: String, bearerToken: String)
