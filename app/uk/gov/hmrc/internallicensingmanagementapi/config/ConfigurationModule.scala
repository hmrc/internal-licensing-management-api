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

package uk.gov.hmrc.internallicensingmanagementapi.config

import javax.inject.{Inject, Provider, Singleton}

import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import uk.gov.hmrc.internallicensingmanagementapi.connectors.{ILMSConfig, PPNSConfig}

class ConfigurationModule extends Module {

  override def bindings(environment: Environment, configuration: Configuration): collection.Seq[Binding[_]] = Seq(
    bind[ILMSConfig].toProvider[ILMSConfigProvider],
    bind[PPNSConfig].toProvider[PPNSConfigProvider]
  )
}

@Singleton
class ILMSConfigProvider @Inject() (val runModeConfiguration: Configuration, environment: Environment, servicesConfig: ServicesConfig)
    extends Provider[ILMSConfig] {

  override def get(): ILMSConfig = {
    val serviceBaseUrl  = servicesConfig.baseUrl("internal-licensing-management")
    val ilmsBearerToken = servicesConfig.getString("internal-licensing-management.bearerToken")
    ILMSConfig(serviceBaseUrl, ilmsBearerToken)
  }
}

@Singleton
class PPNSConfigProvider @Inject() (val runModeConfiguration: Configuration, environment: Environment, servicesConfig: ServicesConfig)
    extends Provider[PPNSConfig] {

  override def get(): PPNSConfig = {
    val serviceBaseUrl = servicesConfig.baseUrl("push-pull-notifications-api")
    PPNSConfig(serviceBaseUrl)
  }
}
