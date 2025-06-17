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

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, StringContextOps}

import uk.gov.hmrc.internallicensingmanagementapi.models.{ILMSRequest, ILMSResponse}

@Singleton
class ILMSConnector @Inject() (http: HttpClientV2, config: ILMSConfig)(implicit ec: ExecutionContext) {

  def send(licenceRef: String, request: ILMSRequest)(implicit hc: HeaderCarrier): Future[(Int, ILMSResponse)] = {
    http.put(url"${config.baseUrl}/customs/licence/$licenceRef")
      .withBody(Json.toJson(request))
      .execute[HttpResponse]
      .map(resp => (resp.status, Json.parse(resp.body).as[ILMSResponse]))
  }
}

case class ILMSConfig(baseUrl: String)
