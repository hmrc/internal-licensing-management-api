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

import play.api.libs.json.{Json, OFormat}

case class ILMSRequest(
    creationDateTime: Option[String],
    correlationId: Option[String],
    sourceSystem: Option[String],
    destinationSystem: Option[String],
    licenceRef: Option[String],
    licenceStatus: Option[String],
    licenceType: Option[String],
    licenceUse: Option[String],
    startDate: Option[String],
    endDate: Option[String],
    licenceLine: Option[Seq[LicenceLine]],
    permittedTrader: Option[Seq[Trader]] = None,
    country: Option[Seq[Country]] = None,
    foreignTrader: Option[Seq[ForeignTrader]] = None,
    restrictions: Option[Seq[Restriction]] = None
  )

object ILMSRequest {
  implicit val requestFormat: OFormat[ILMSRequest] = Json.format[ILMSRequest]
}
case class Restriction(text: Option[String])

object Restriction {
  implicit val restrictionFormat: OFormat[Restriction] = Json.format[Restriction]
}
case class Country(countryCode: Option[String], countryGroup: Option[String], countryUse: Option[String])

object Country {
  implicit val countryFormat: OFormat[Country] = Json.format[Country]
}

case class ForeignTrader(
    name: Option[String],
    address1: Option[String],
    address2: Option[String],
    address3: Option[String],
    address4: Option[String],
    address5: Option[String],
    postcode: Option[String],
    countryCode: Option[String]
  )

object ForeignTrader {
  implicit val foreignFormat: OFormat[ForeignTrader] = Json.format[ForeignTrader]
}

case class Trader(
    eori: Option[String],
    startDate: Option[String],
    endDate: Option[String],
    name: Option[String],
    address1: Option[String],
    address2: Option[String],
    address3: Option[String],
    address4: Option[String],
    address5: Option[String],
    postcode: Option[String],
    countryCode: Option[String]
  )

object Trader {
  implicit val traderFormat: OFormat[Trader] = Json.format[Trader]
}

case class LicenceLine(
    commodity: Option[Seq[Commodity]],
    goodsDescription: Option[String],
    controlledBy: Option[String],
    quantityUnit: Option[String],
    quantityIssued: Option[BigDecimal],
    lineNumber: Option[BigDecimal]
  )

object LicenceLine {
  implicit val licenceLineFormat: OFormat[LicenceLine] = Json.format[LicenceLine]
}

case class Commodity(
    commodityCode: Option[String] = None,
    supplement: Option[String] = None
  )

object Commodity {
  implicit val commodityFormat: OFormat[Commodity] = Json.format[Commodity]
}
