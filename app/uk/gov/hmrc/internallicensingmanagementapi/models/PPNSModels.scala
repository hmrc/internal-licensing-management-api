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

package uk.gov.hmrc.internallicensingmanagementapi.models

import java.util.UUID

import play.api.libs.json.{Format, Json, OFormat}

case class Box(boxId: BoxId)

object Box {
  implicit val format: OFormat[Box] = Json.format[Box]
}

case class BoxId(value: UUID) extends AnyVal {
  override def toString: String = value.toString
}

object BoxId {
  implicit val format: Format[BoxId] = Json.valueFormat[BoxId]
}

case class NotificationId(value: UUID) extends AnyVal {
  override def toString: String = value.toString
}

object NotificationId {
  implicit val format: Format[NotificationId] = Json.valueFormat[NotificationId]
}
case class CreateNotificationResponse(notificationId: NotificationId)

object CreateNotificationResponse {
  implicit val format: OFormat[CreateNotificationResponse] = Json.format[CreateNotificationResponse]
}
trait PPNSError
case class BoxNotFound() extends PPNSError