package uk.gov.hmcts.ccd.corecasedata.simulations

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.corecasedata.scenarios.api._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils._
import scala.concurrent.duration._

class CCDPipelineSimulation extends Simulation  {

  val BaseURL = Environment.baseURL
  val config: Config = ConfigFactory.load()
  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    .doNotTrackHeader("1")

  val CCDOvernightPipelineE2E = scenario("CCDOvernight").repeat(1) {
    exec(
      // Browse.Homepage,
      // DVExcep.DVLogin,
      // DVExcep.DVCreateCase,
      // DVExcep.DVDocUpload,
      // DVExcep.DVSearchAndView,
      // Logout.ccdLogout,
      Browse.Homepage,
      CMC.CMCLogin,
      CMC.CMCCreateCase,
      CMC.CMCSubmitCase,
      CMC.CMCSearchAndView,
      Logout.ccdLogout,
      Browse.Homepage,
      PBGoR.PBLogin,
      PBGoR.PBCreateCase,
      PBGoR.PBPaymentSuccessful,
      PBGoR.PBDocUpload,
      PBGoR.PBSearchAndView,
      Logout.ccdLogout,
      Browse.Homepage,
      SSCS.SSCSLogin,
      SSCS.SSCSCreateCase,
      SSCS.SSCSDocUpload,
      SSCS.SSCSSearchAndView,
      Logout.ccdLogout
    )
  }

  val CCDAPIOvernightPipeline = scenario("CCD API Pipeline").repeat(1)
  {
    exec(ccddatastore.S2SLogin)
    .exec(ccddatastore.idamLogin)
    .exec(ccddatastore.CCDAPI_ProbateCreate)
    .exec(ccddatastore.CCDAPI_ProbateCaseEvents)
  }

  setUp(
      // CCDOvernightPipelineE2E.inject(rampUsers(1) during (1 minutes))
      CCDAPIOvernightPipeline.inject(rampUsers(5) during (1 minutes)) 
  )
    .protocols(httpProtocol)
    .assertions(global.successfulRequests.percent.is(100))
}