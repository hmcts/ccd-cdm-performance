package uk.gov.hmcts.ccd.corecasedata.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils.Environment

import scala.concurrent.duration._

class CCDProbateSimulation extends Simulation  {

  val BaseURL = Environment.baseURL

  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    .proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .doNotTrackHeader("1")

  val CCDProbateScenario = scenario("CCDUI").repeat(100)
  {
    exec(
      Browse.Homepage,
      ExecuteLogin.submitLogin,
      //ProbateSearch.ProbateLogin,
      PBGoR.PBCreateCase,
      PBGoR.PBPaymentSuccessful,
      PBGoR.PBDocUpload,
      PBGoR.PBSearchAndView,
      //PBGoR.PrintCaseID,
      Logout.ccdLogout)
      //WaitforNextIteration.waitforNextIteration)
  }

  //setUp(CCDProbateScenario.inject(atOnceUsers(1))).protocols(httpProtocol)
  setUp(CCDProbateScenario
    .inject(rampUsers(100) during (20 minutes))
    .protocols(httpProtocol))
    .maxDuration(80 minutes)
}

