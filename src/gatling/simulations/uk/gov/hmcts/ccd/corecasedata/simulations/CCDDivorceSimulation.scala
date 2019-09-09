package uk.gov.hmcts.ccd.corecasedata.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils.Environment

import scala.concurrent.duration._

class CCDDivorceSimulation extends Simulation  {

  val BaseURL = Environment.baseURL

  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    .proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
    .doNotTrackHeader("1")

  val CCDDivorceScenario = scenario("CCDUI").repeat(1)
  {
    exec(
      //Logout.ccdLogout,
      Browse.Homepage,
      ExecuteLogin.submitLogin,
      DVExcep.DVCreateCase,
      DVExcep.DVDocUpload,
      DVExcep.DVSearchAndView,
      Logout.ccdLogout,
      //WaitforNextIteration.waitforNextIteration
     )
  }

  //setUp(CCDDivorceScenario.inject(atOnceUsers(1))).protocols(httpProtocol)
  setUp(CCDDivorceScenario
    .inject(rampUsers(1) during (1 minutes))
    .protocols(httpProtocol))
    //.maxDuration(1 minutes)
}