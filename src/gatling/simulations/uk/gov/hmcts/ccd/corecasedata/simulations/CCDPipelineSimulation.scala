package uk.gov.hmcts.ccd.corecasedata.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils._

import scala.concurrent.duration._

class CCDPipelineSimulation extends Simulation  {

  val BaseURL = Environment.baseURL

  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    .doNotTrackHeader("1")

  val CCDOvernightPipelineE2E = scenario( "CCDOvernight").repeat(1) {
    exec(
      Browse.Homepage,
      DVExcep.DVLogin,
      DVExcep.DVCreateCase,
      DVExcep.DVDocUpload,
      DVExcep.DVSearchAndView,
      Logout.ccdLogout,
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

  setUp(
      CCDOvernightPipelineE2E.inject(rampUsers(1) during (1 minutes))
  )
    .protocols(httpProtocol)
}