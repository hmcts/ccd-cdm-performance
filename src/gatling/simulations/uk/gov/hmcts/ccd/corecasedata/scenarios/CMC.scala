package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils.Environment

import scala.concurrent.duration._

object CMC {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val CCDEnvurl = Environment.ccdEnvurl
  val CommonHeader = Environment.commonHeader
  val idam_header = Environment.idam_header
  val feedUserData = csv("CMCUserData.csv").circular
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val cmcCaseActivityRepeat = 2

  val headers_0 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_1 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_2 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_7 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val headers_8 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_11 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_13 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-banners.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val headers_14 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val headers_16 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val headers_23 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val CMCLogin = group("CMC_Login") {

    exec(http("CDM_020_005_Login")
      .post(IdamURL + "/login?response_type=code&client_id=ccd_gateway&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
      .disableFollowRedirect
      .headers(idam_header)
      .formParam("username", "${CMCUserName}")
      .formParam("password", "${CMCUserPassword}")
      .formParam("save", "Sign in")
      .formParam("selfRegistrationEnabled", "false")
      .formParam("_csrf", "${csrf}")
      .check(headerRegex("Location", "(?<=code=)(.*)&client").saveAs("authCode"))
      .check(status.in(200, 302)))

      .exec(http("CDM_020_010_Login")
        .get(CCDEnvurl)
        .headers(CommonHeader)) //1

      .exec(http("CDM_020_015_Login")
        .options(BaseURL + "/oauth2?code=${authCode}&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
        .headers(CommonHeader)) //2

      .exec(http("CDM_020_020_Login")
        .get(BaseURL + "/oauth2?code=${authCode}&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
        .headers(CommonHeader)) //3

      .exec(http("CDM_020_025_Login")
        .get(CCDEnvurl)
        .headers(CommonHeader)) //1

      .exec(http("CDM_020_030_Login")
        .options(BaseURL + "/data/internal/profile")
        .headers(CommonHeader)) //5

      .exec(http("CDM_020_035_Login")
        .options(BaseURL + "/activity/cases/0/activity")
        .headers(headers_2)) //2

      .exec(http("CDM_020_040_Login")
        .get(BaseURL + "/data/internal/profile")
        .headers(headers_7)) //7

      .exec(http("CDM_020_045_Login")
        .options(BaseURL + "/data/internal/banners/?ids=${CMCJurisdiction}")
        .headers(CommonHeader)) //5

      .exec(http("CDM_020_050_Login")
        .options(BaseURL + "/data/internal/case-types/${CMCCaseType}/work-basket-inputs")
        .headers(CommonHeader)) //5

      .exec(http("CDM_020_055_Login")
        .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${CMCJurisdiction}/case-types/${CMCCaseType}/cases?view=WORKBASKET&state=create&page=1")
        .headers(CommonHeader)) //2

      .exec(http("CDM_020_060_Login")
        .get(BaseURL + "/activity/cases/0/activity")
        .headers(CommonHeader)) //3

      .exec(http("CDM_020_065_Login")
        .options(BaseURL + "/data/caseworkers/:uid/jurisdictions/${CMCJurisdiction}/case-types/${CMCCaseType}/cases/pagination_metadata?state=create")
        .headers(CommonHeader)) //2

      .exec(http("CDM_020_070_Login")
        .get(BaseURL + "/data/internal/banners/?ids=${CMCJurisdiction}")
        .headers(headers_13)) //13

      .exec(http("CDM_020_075_Login")
        .get(BaseURL + "/data/internal/case-types/${CMCCaseType}/work-basket-inputs")
        .headers(headers_14)) //14

      .exec(http("CDM_020_080_Login")
        .get(BaseURL + "/data/caseworkers/:uid/jurisdictions/${CMCJurisdiction}/case-types/${CMCCaseType}/cases/pagination_metadata?state=create")
        .headers(CommonHeader)) //3

      .exec(http("CDM_020_085_Login")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${CMCJurisdiction}/case-types/${CMCCaseType}/cases?view=WORKBASKET&state=create&page=1")
        .headers(CommonHeader)) //3

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val CMCCreateCase = group("CMC_Create") {
    exec(http("CMC_030_005_CreateCasePage")
      .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=create")
      .headers(CommonHeader))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("CMC_030_010_CreateCaseDetails1")
      .get("/data/internal/case-types/${CMCCaseType}/event-triggers/CreateClaim?ignore-warning=false")
      .headers(headers_1)
      .check(jsonPath("$.event_token").saveAs("New_Case_event_token")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("CMC_030_015_CreateCaseDetails2")
      .post(BaseURL + "/data/caseworkers/:uid/jurisdictions/${CMCJurisdiction}/case-types/${CMCCaseType}/cases?ignore-warning=false")
      .headers(CommonHeader)
      .body(StringBody("{\n  \"data\": {},\n  \"event\": {\n    \"id\": \"CreateClaim\",\n    \"summary\": \"pipeline-test1202\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${New_Case_event_token}\",\n  \"ignore_warning\": false,\n  \"draft_id\": null\n}"))
      .check(jsonPath("$.id").saveAs("New_Case_Id")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val CMCSubmitCase = group("CMC_Submit") {
    exec(http("CMC_040_005_SubmitCase")
      .get("/data/internal/cases/${New_Case_Id}/event-triggers/IssueClaim?ignore-warning=false")
      .headers(headers_16)
      .check(jsonPath("$.event_token").saveAs("existing_case_event_token")))

    .exec(http("CMC_040_010_SubmitCase")
      .post("/data/cases/${New_Case_Id}/events")
      .headers(headers_23)
      .body(StringBody("{\n  \"data\": {},\n  \"event\": {\n    \"id\": \"IssueClaim\",\n    \"summary\": \"pipeline-claim-submitted\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false\n}")))

    .repeat(cmcCaseActivityRepeat) {
      exec(http("CMC_CaseActivity")
        .get("/activity/cases/${New_Case_Id}/activity")
        .headers(headers_2))

        .pause(3)
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val CMCSearchAndView = group("CMC_View") {
    exec(http("CMC_050_005_SearchPage")
      .get("/data/internal/case-types/${CMCCaseType}/work-basket-inputs")
      .headers(headers_0))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("CMC_050_010_SearchForCase")
      .get("/aggregated/caseworkers/:uid/jurisdictions/${CMCJurisdiction}/case-types/${CMCCaseType}/cases?view=WORKBASKET&page=1&case_reference=${New_Case_Id}")
      .headers(CommonHeader))

    .exec(http("CMC_050_015_SearchForCase")
      .get("/data/caseworkers/:uid/jurisdictions/${CMCJurisdiction}/case-types/${CMCCaseType}/cases/pagination_metadata?case_reference=${New_Case_Id}")
      .headers(CommonHeader))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("CMC_050_020_OpenCase")
      .get("/data/internal/cases/${New_Case_Id}")
      .headers(headers_8))

    .repeat(cmcCaseActivityRepeat) {
      exec(http("CMC_CaseActivity")
        .get("/activity/cases/${New_Case_Id}/activity")
        .headers(headers_2))

        .pause(3)
    }
  }
}
