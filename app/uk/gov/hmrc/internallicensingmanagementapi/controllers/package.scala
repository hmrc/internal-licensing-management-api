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

import scala.util.control.NonFatal

import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._
import uk.gov.hmrc.auth.core.UnsupportedAuthProvider

import uk.gov.hmrc.internallicensingmanagementapi.models.ILMSResponse

package object controllers {
  val logger = Logger("application")

  def recovery: PartialFunction[Throwable, Result] = {
    case _: UnsupportedAuthProvider =>
      val errorMessage = "Only privileged applications can access this API"
      logger.error(errorMessage)
      Forbidden(Json.toJson(ILMSResponse.errorResponse(errorMessage)))

    case NonFatal(e) =>
      logger.error("An unexpected error occurred:", e)
      InternalServerError(Json.toJson(ILMSResponse.errorResponse("An unexpected error occurred")))
  }
}
