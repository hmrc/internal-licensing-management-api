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

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

import controllers.recovery

import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import uk.gov.hmrc.auth.core.AuthProvider.PrivilegedApplication
import uk.gov.hmrc.auth.core.{AuthProviders, AuthorisedFunctions}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import uk.gov.hmrc.internallicensingmanagementapi.connectors.{AuthConnector, ILMSConnector}
import uk.gov.hmrc.internallicensingmanagementapi.controllers.actions.{BearerAction, ClientIdAction}
import uk.gov.hmrc.internallicensingmanagementapi.models.{BoxNotFound, CreateNotificationResponse, ILMSRequest}
import uk.gov.hmrc.internallicensingmanagementapi.services.NotificationService

@Singleton()
class ILMSController @Inject() (
    cc: ControllerComponents,
    ilmsConnector: ILMSConnector,
    val authConnector: AuthConnector,
    notificationService: NotificationService,
    bearerAction: BearerAction,
    clientIdAction: ClientIdAction
  )(implicit ec: ExecutionContext
  ) extends BackendController(cc) with AuthorisedFunctions {

  def licence(licenceRef: String): Action[ILMSRequest] = Action.async(controllerComponents.parsers.json[ILMSRequest]) {
    implicit request =>
      authorised(AuthProviders(PrivilegedApplication)) {
        ilmsConnector
          .send(licenceRef, request.body)
          .map(resp => Status(resp._1)(Json.toJson(resp._2)))
      } recover recovery
  }

  def notifyUsage(): Action[JsValue] = (Action andThen bearerAction andThen clientIdAction).async(controllerComponents.parsers.json) {
    implicit requestWithClientId =>
      implicit val request: Request[JsValue] = requestWithClientId.request
      notificationService.notifyUsage(requestWithClientId.clientId, requestWithClientId.request.body).map {
        case Right(CreateNotificationResponse(_)) => NoContent
        case Left(BoxNotFound())                  => NotFound(Json.toJson(Map("error" -> "Box not found")))
        case Left(_)                              => InternalServerError
      } recover recovery
  }
}
