package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils.Environment
import scala.concurrent.duration._

object PBGoR {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val CCDEnvurl = Environment.ccdEnvurl
  val CommonHeader = Environment.commonHeader
  val idam_header = Environment.idam_header
  val feedUserData = csv("ProbateUserData.csv").circular
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Origin" -> "https://idam-web-public.aat.platform.hmcts.net",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_1 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin")

  val headers_2 = Map(
    "Access-Control-Request-Headers" -> "content-type",
    "Access-Control-Request-Method" -> "GET",
    "Origin" -> "https://ccd-case-management-web-aat.service.core-compute-aat.internal",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site")

  val headers_3 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> "https://ccd-case-management-web-aat.service.core-compute-aat.internal",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site")

  val headers_4 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_5 = Map(
    "Access-Control-Request-Headers" -> "content-type,experimental",
    "Access-Control-Request-Method" -> "GET",
    "Origin" -> "https://ccd-case-management-web-aat.service.core-compute-aat.internal",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site")

  val headers_6 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_7 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_8 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://ccd-case-management-web-aat.service.core-compute-aat.internal",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val headers_13 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-banners.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://ccd-case-management-web-aat.service.core-compute-aat.internal",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val headers_14 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://ccd-case-management-web-aat.service.core-compute-aat.internal",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val headers_15 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "none",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_22 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val PBLogin = group("PB_Login") {

    exec(http("CDM_020_005_Login")
      .post(IdamURL + "/login?response_type=code&client_id=ccd_gateway&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
      .disableFollowRedirect
      .headers(idam_header)
      .formParam("username", "${ProbateUserName}")
      .formParam("password", "${ProbateUserPassword}")
      .formParam("save", "Sign in")
      .formParam("selfRegistrationEnabled", "false")
      .formParam("_csrf", "${csrf}")
      .check(headerRegex("Location", "(?<=code=)(.*)&scope").saveAs("authCode"))
      .check(status.in(200, 302)))

    .exec(http("CDM_020_010_Login")
      .get(CCDEnvurl)
      .headers(headers_1))

    .exec(http("CDM_020_015_Login")
      .options(BaseURL + "/oauth2?code=${authCode}&redirect_uri=ccd-case-management-web-aat.service.core-compute-aat.internal/oauth2redirect")
      .headers(headers_2))

    .exec(http("CDM_020_020_Login")
      .get(BaseURL + "/oauth2?code=${authCode}&redirect_uri=ccd-case-management-web-aat.service.core-compute-aat.internal/oauth2redirect")
      .headers(headers_3))

    .exec(http("CDM_020_025_Login")
      .get(CCDEnvurl)
      .headers(headers_1))

    .exec(http("CDM_020_030_Login")
      .options(BaseURL + "/data/internal/profile")
      .headers(headers_5))

    .exec(http("CDM_020_035_Login")
      .options(BaseURL + "/activity/cases/0/activity")
      .headers(headers_2))

    .exec(http("CDM_020_040_Login")
      .get(BaseURL + "/activity/cases/0/activity")
      .headers(headers_3))

    .exec(http("CDM_020_045_Login")
      .get(BaseURL + "/data/internal/profile")
      .headers(headers_8))

    .exec(http("CDM_020_050_Login")
      .options(BaseURL + "/data/internal/banners/?ids=${PBJurisdiction}")
      .headers(headers_5))

    .exec(http("CDM_020_055_Login")
      .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases?view=WORKBASKET&state=CaseCreated&page=1")
      .headers(headers_2))

    .exec(http("CDM_020_060_Login")
      .options(BaseURL + "/data/internal/case-types/${PBCaseType}/work-basket-inputs")
      .headers(headers_5))

    .exec(http("CDM_020_065_Login")
      .options(BaseURL + "/data/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases/pagination_metadata?state=CaseCreated")
      .headers(headers_2))

    .exec(http("CDM_020_070_Login")
      .get(BaseURL + "/data/internal/banners/?ids=${PBJurisdiction}")
      .headers(headers_13))

    .exec(http("CDM_020_075_Login")
      .get(BaseURL + "/data/internal/case-types/${PBCaseType}/work-basket-inputs")
      .headers(headers_14))

    .exec(http("CDM_020_080_Login")
      .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases?view=WORKBASKET&state=CaseCreated&page=1")
      .headers(headers_3))

    .exec(http("CDM_020_085_Login")
      .get(BaseURL + "/data/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases/pagination_metadata?state=CaseCreated")
      .headers(headers_3))
  }

  val PBCreateCase = group("PB_Create") {
    exec(http("PBGoR_030_005_CreateCase")
      .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=create")
      .headers(CommonHeader))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

      .exec(http("PBGoR_030_010_CreateCase")
      .get(BaseURL + "/data/internal/case-types/${PBCaseType}/event-triggers/applyForGrant?ignore-warning=false")
      .headers(headers_4)
      .check(jsonPath("$.event_token").saveAs("New_Case_event_token")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("PBGoR_030_015_CreateCase")
      .post("/data/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases?ignore-warning=false")
      .headers(CommonHeader)
      .body(StringBody("{\n  \"data\": {},\n  \"event\": {\n    \"id\": \"applyForGrant\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${New_Case_event_token}\",\n  \"ignore_warning\": false,\n  \"draft_id\": null\n}"))
      .check(jsonPath("$.id").saveAs("New_Case_Id")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val PBPaymentSuccessful = group("PB_Payment") {
    exec(http("PBGoR_040_005_PaymentSuccessful")
      .get("/data/internal/cases/${New_Case_Id}/event-triggers/paymentSuccessApp?ignore-warning=false")
      .headers(headers_6)
      .check(jsonPath("$.event_token").saveAs("existing_case_event_token")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

      .exec(http("PBGoR_040_010_PaymentSuccessful")
      .post("/data/case-types/${PBCaseType}/validate?pageId=paymentSuccessAppboPaymentSuccessfulAppPage1")
      .headers(headers_22) //4
      .body(StringBody("{\n  \"data\": {\n    \"applicationSubmittedDate\": \"2020-02-01\"\n  },\n  \"event\": {\n    \"id\": \"paymentSuccessApp\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false,\n  \"event_data\": {\n    \"applicationSubmittedDate\": \"2020-02-01\"\n  },\n  \"case_reference\": \"${New_Case_Id}\"\n}")))

    .exec(http("PBGoR_040_015_PaymentSuccessful")
      .post("/data/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases/${New_Case_Id}/events")
      .headers(CommonHeader)
      .body(StringBody("{\n  \"data\": {\n    \"applicationSubmittedDate\": \"2020-02-01\"\n  },\n  \"event\": {\n    \"id\": \"paymentSuccessApp\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false\n}")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val PBDocUpload = group("PB_DocUpload") {
    exec(http("PGBoR_050_005_DocumentUpload")
      .get("/data/internal/cases/${New_Case_Id}/event-triggers/boUploadDocumentsForCaseCreated?ignore-warning=false")
      .headers(headers_6)
      .check(jsonPath("$.event_token").saveAs("existing_case_event_token")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("PGBoR_050_010_DocumentUpload")
      .post(BaseURL + "/documents")
      .bodyPart(RawFileBodyPart("files", "1MB.pdf")
        .fileName("1MB.pdf")
        .transferEncoding("binary"))
      .asMultipartForm
      .formParam("classification", "PUBLIC")
      .check(status.is(200))
      .check(regex("""http://(.+)/""").saveAs("DMURL"))
      .check(regex("""/documents/(.+)"""").saveAs("Document_ID")))

    .exec(http("PGBoR_050_015_DocumentUpload")
      .post("/data/case-types/${PBCaseType}/validate?pageId=boUploadDocumentsForCaseCreatedboUploadDocumentPage1")
      .headers(headers_22) //4
      .body(StringBody("{\n  \"data\": {\n    \"boDocumentsUploaded\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"DocumentType\": \"deathCertificate\",\n          \"Comment\": \"test 1mb file\",\n          \"DocumentLink\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          }\n        }\n      }\n    ]\n  },\n  \"event\": {\n    \"id\": \"boUploadDocumentsForCaseCreated\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false,\n  \"event_data\": {\n    \"boDocumentsUploaded\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"DocumentType\": \"deathCertificate\",\n          \"Comment\": \"test 1mb file\",\n          \"DocumentLink\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          }\n        }\n      }\n    ]\n  },\n  \"case_reference\": \"${New_Case_Id}\"\n}")))

    .exec(http("PGBoR_050_020_DocumentUpload")
      .post("/data/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases/${New_Case_Id}/events")
      .headers(CommonHeader)
      .body(StringBody("{\n  \"data\": {\n    \"boDocumentsUploaded\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"DocumentType\": \"deathCertificate\",\n          \"Comment\": \"test 1mb file\",\n          \"DocumentLink\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          }\n        }\n      }\n    ]\n  },\n  \"event\": {\n    \"id\": \"boUploadDocumentsForCaseCreated\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false\n}")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val PBSearchAndView = group("PB_View") {
    exec(http("PBGoR_060_005_SearchAndView")
      .get("/data/caseworkers/:uid/jurisdictions/${PBJurisdiction}/case-types/${PBCaseType}/cases/pagination_metadata")//?case_reference=1566214443240990")
      .headers(CommonHeader))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("PBGoR_060_010_SearchAndView")
      .get("/data/internal/cases/${New_Case_Id}")
      .headers(headers_7))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("PBGoR_060_015_SearchAndView")
      .get("/documents/${Document_ID}/binary")
      .headers(headers_15)) //15

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }
}