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

package uk.gov.hmrc.internallicensingmanagementapi.controllers.actions

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

import play.api.mvc.Results.BadRequest
import play.api.mvc.{ActionRefiner, Request, Result}
import uk.gov.hmrc.apiplatform.modules.common.domain.models.ClientId
import uk.gov.hmrc.http.HttpErrorFunctions

case class RequestWithClientId[A](request: Request[A], clientId: ClientId)

@Singleton
class ClientIdAction @Inject() ()(implicit ec: ExecutionContext) extends ActionRefiner[Request, RequestWithClientId]
    with HttpErrorFunctions {
  actionName =>
  override def executionContext: ExecutionContext = ec

  override protected def refine[A](request: Request[A]): Future[Either[Result, RequestWithClientId[A]]] = {
    request.headers.get("X-Client-Id") match {
      case Some(value) => Future.successful(Right(RequestWithClientId(request, ClientId(value))))
      case None        => Future.successful(Left(BadRequest))
    }
  }

}
