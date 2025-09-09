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

import scala.io.Source

import play.api.libs.json.Json
import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class ILMSResponseSpec extends BaseJsonFormattersSpec with TestData {

  "given a ILMSResponse" should {

    "produce response json" in {
      Json.toJson(Response.example) shouldBe Json.parse(Source.fromResource("./ilms-response-valid.json").mkString)
    }

    "produce error json" in {
      Json.toJson(Response.error) shouldBe Json.parse(Source.fromResource("./ilms-error-valid.json").mkString)

    }

    "read response json" in {
      val json = Source.fromResource("./ilms-response-valid.json").mkString

      testFromJson[ILMSResponse](json)(Response.example)
    }

    "Ignore extra fields in response json" in {
      val json = Source.fromResource("./ilms-response-extra.json").mkString

      testFromJson[ILMSResponse](json)(Response.example)
    }

    "read error json" in {
      val json = Source.fromResource("./ilms-error-valid.json").mkString

      testFromJson[ILMSResponse](json)(Response.error)
    }
  }
}
