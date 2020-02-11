package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils.Environment

object EthosSearchView {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val CCDEnvurl = Environment.ccdEnvurl
  val CommonHeader = Environment.commonHeader
  val idam_header = Environment.idam_header
  val feedUserData = csv("CMCUserData.csv").circular
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val headers_0 = Map(
    "Access-Control-Request-Headers" -> "content-type",
    "Access-Control-Request-Method" -> "GET",
    "Origin" -> "https://ccd-case-management-web-perftest.service.core-compute-perftest.internal",
    "Sec-Fetch-Mode" -> "no-cors")

  val headers_2 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> "https://ccd-case-management-web-perftest.service.core-compute-perftest.internal",
    "Sec-Fetch-Mode" -> "cors")

  val Search = exec(http("request_0")
      .options("/aggregated/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/Manchester_Dev/cases?view=WORKBASKET&page=1")
      .headers(headers_0))

    .exec(http("request_1")
      .options("/data/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/Manchester_Dev/cases/pagination_metadata")
      .headers(headers_0))

    .exec(http("request_2")
      .get("/data/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/Manchester_Dev/cases/pagination_metadata")
      .headers(headers_2))

    .exec(http("request_3")
      .get("/aggregated/caseworkers/:uid/jurisdictions/EMPLOYMENT/case-types/Manchester_Dev/cases?view=WORKBASKET&page=1")
      .headers(headers_2))


}

