import sbt.*

object AppDependencies {

  private val bootstrapVersion = "10.3.0"
  val commonDomainVersion      = "0.19.0"

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "uk.gov.hmrc" %% "api-platform-common-domain" % commonDomainVersion
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30"              % bootstrapVersion    % Test,
    "uk.gov.hmrc" %% "api-platform-common-domain-fixtures" % commonDomainVersion % Test
  )

  val it = Seq.empty
}
