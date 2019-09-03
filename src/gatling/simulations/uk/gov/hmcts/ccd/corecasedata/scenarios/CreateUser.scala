package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils.Environment

object CreateUser {

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val CommonHeader = Environment.commonHeader
  val CCDAPIEnvurl = Environment.baseURL

  val headers_1 = Map(
    "Authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjY2RfZGVmaW5pdGlvbiIsImV4cCI6MTU2NzEwNzAwMn0.Q5FyjRzHnytXsW6rZ_Ri0yjP3ydK0cvx5ToWagacplV9_3ER27rJ43DvDbfoCftr3lfmzmT_DpOLaFZuUJrbQA",
    "Content-Type" -> "application/json")

  val CreateUserProfile = exec(http("request_4")
    .post("http://ccd-user-profile-api-perftest.service.core-compute-perftest.internal/b")
    //.post("https://ccd-api-gateway-web-perftest.service.core-compute-perftest.internal/user-profile/users")
    .headers(headers_1)
      .body(StringBody("{\"id\": \"ccdloadtest4@gmail.com\",\"jurisdictions\": [{\"id\": \"CMC\"}],\"work_basket_default_jurisdiction\": \"CMC\",\"work_basket_default_case_type\": \"MoneyClaimCase\",\"work_basket_default_state\": \"open\"}\"")))

    /*.formParam("_csrf", "${csrf}")
    .formParam("currentjurisdiction", "CMC")
    .formParam("idamId", "ccdloadtest4@gmail.com")
    .formParam("jurisdiction", "CMC")
    .formParam("jurisdictionDropdown", "CMC")
    .formParam("caseTypeDropdown", "MoneyClaimCase")
    .formParam("stateDropdown", "open"))*/

}
