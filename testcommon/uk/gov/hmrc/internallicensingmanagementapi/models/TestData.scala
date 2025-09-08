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

trait TestData {

  object Response {
    val example = ILMSResponse(Some("123e4567-e89b-12d3-a456-426655440000"), Some("CDS"), Some("ILBDOTI"), Some("GBOIL123456"), Some("ACCEPTED"), None)

    val error = ILMSResponse(
      Some("123e4567-e89b-12d3-a456-426655440000"),
      Some("CDS"),
      Some("ILBDOTI"),
      Some("GBOIL123456"),
      Some("REJECTED"),
      Some(List(ILMSError(Some("LIC.ERR.02"), Some("Duplicate Foreign Traders not allowed"), Some("/foreignTrader[1]/name"))))
    )
  }

  object Request {

    val example = ILMSRequest(
      Some("2024-01-15T15:18:33+00:00"),
      Some("123e4567-e89b-12d3-a456-426655440003"),
      Some("ILBDOTI"),
      Some("CDS"),
      Some("GBOIL123456"),
      Some("O"),
      Some("OGE"),
      Some("E"),
      Some("1998-01-15"),
      Some("2077-01-15"),
      Some(List(
        LicenceLine(
          Some(List(Commodity(Some("101010"), Some("SC11")), Commodity(Some("101020"), Some("SC12")), Commodity(Some("101030"), Some("SC13")))),
          Some("Material pack goods description"),
          Some("Q"),
          Some("KGM"),
          Some(8009270.235),
          Some(1)
        ),
        LicenceLine(
          Some(List(Commodity(Some("102010"), Some("SC21")), Commodity(Some("102020"), Some("SC22")), Commodity(Some("102030"), Some("SC33")))),
          None,
          Some("Q"),
          Some("KGM"),
          Some(8009270.235),
          Some(2)
        ),
        LicenceLine(
          Some(List(Commodity(Some("103010"), Some("SC31")), Commodity(Some("103020"), Some("SC32")), Commodity(Some("103030"), Some("SC33")))),
          Some("Material pack goods description"),
          Some("Q"),
          Some("KGM"),
          Some(8009270.235),
          Some(3)
        )
      )),
      Some(List(
        Trader(
          Some("GB123456789000"),
          Some("1998-01-15"),
          Some("2077-01-15"),
          Some("Company Name Ltd"),
          Some("21 The Street"),
          Some("Romford"),
          Some("Essex"),
          Some("Essex4"),
          Some("Essex5"),
          Some("RM12DE"),
          Some("UK")
        ),
        Trader(
          Some("GB234567891000"),
          Some("1998-01-15"),
          Some("2077-01-15"),
          Some("New Company Name Ltd"),
          Some("Alexander House"),
          Some("21 Victoria Avenue"),
          Some("Southend-on-Sea"),
          Some("Essex"),
          Some("Essex5"),
          Some("SS991AA"),
          Some("UK")
        )
      )),
      Some(List(Country(None, Some("A016"), Some("E")), Country(Some("AD"), None, Some("E")), Country(Some("AE"), None, Some("E")))),
      Some(List(ForeignTrader(
        Some("Pete Truman"),
        Some("Washington Industrial Park"),
        Some("Alexander House"),
        Some("Southend-on-Sea"),
        Some("Essex"),
        Some("Essex5"),
        Some("SS991AA"),
        Some("US")
      ))),
      Some(List(Restriction(Some("Not to be used for anything illegal")), Restriction(Some("Some other free text"))))
    )
  }
}
