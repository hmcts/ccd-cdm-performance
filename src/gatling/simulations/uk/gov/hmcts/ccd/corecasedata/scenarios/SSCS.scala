package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios.utils.Environment
import scala.concurrent.duration._

object SSCS {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val CCDEnvurl = Environment.ccdEnvurl
  val CommonHeader = Environment.commonHeader
  val idam_header = Environment.idam_header
  val feedUserData = csv("SSCSUserData.csv").circular
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val headers_0 = Map(
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

  val headers_3 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> "CCDEnvurl",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site")

  val headers_8 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Mode" -> "cors",
    "experimental" -> "true")

  val headers_9 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Sec-Fetch-Mode" -> "cors",
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

  val headers_28 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> CCDEnvurl,
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "cross-site",
    "experimental" -> "true")

  val SSCSLogin = group("SSCS_Login") {

    exec(http("CDM_020_005_Login")
      .post(IdamURL + "/login?response_type=code&client_id=ccd_gateway&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
      .disableFollowRedirect
      .headers(idam_header)
      .formParam("username", "${SSCSUserName}")
      .formParam("password", "${SSCSUserPassword}")
      .formParam("save", "Sign in")
      .formParam("selfRegistrationEnabled", "false")
      .formParam("_csrf", "${csrf}")
      .check(headerRegex("Location", "(?<=code=)(.*)&client").saveAs("authCode"))
      .check(status.in(200, 302)))

    .exec(http("CDM_020_010_Login")
      .get(CCDEnvurl + "/config")
      .headers(CommonHeader))

    .exec(http("CDM_020_015_Login")
      .options(BaseURL + "/oauth2?code=${authCode}&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
      .headers(CommonHeader))

    .exec(http("CDM_020_020_Login")
      .get(BaseURL + "/oauth2?code=${authCode}&redirect_uri=" + CCDEnvurl + "/oauth2redirect")
      .headers(CommonHeader))

    .exec(http("CDM_020_025_Login")
      .get(CCDEnvurl + "/config")
      .headers(CommonHeader))

    .exec(http("CDM_020_030_Login")
      .options(BaseURL + "/data/caseworkers/:uid/profile"))

    .exec(http("CDM_020_035_Login")
      .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types?access=read"))

    .exec(http("CDM_020_040_Login") //slow step???
      .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types?access=read")
      .headers(CommonHeader))

    .exec(http("CDM_020_045_Login")
      .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/work-basket-inputs"))

    .exec(http("CDM_020_050_Login")
      .options(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/cases?view=WORKBASKET&state=TODO&page=1"))

    .exec(http("CDM_020_055_Login")
      .options(BaseURL + "/data/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/cases/pagination_metadata?state=TODO"))

    .exec(http("CDM_020_060_Login")
      .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/work-basket-inputs")
      .headers(CommonHeader))

    .exec(http("CDM_020_065_Login")
      .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/cases?view=WORKBASKET&state=TODO&page=1")
      .headers(CommonHeader))

    .exec(http("CDM_020_070_Login")
      .get(BaseURL + "/data/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/cases/pagination_metadata?state=TODO")
      .headers(headers_3))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val SSCSCreateCase = group("SSCS_Create") {

    exec(http("SSCS_020_005_CreateCase")
      .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
      .headers(CommonHeader))

    .exec(http("SSCS_020_010_CreateCase")
      .get("/data/internal/case-types/${SSCSCaseType}/event-triggers/appealCreated?ignore-warning=false")
      .check(jsonPath("$.event_token").saveAs("New_Case_event_token"))
      .headers(headers_1))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("SSCS_020_015_CreateCase")
      .post("/data/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/cases?ignore-warning=false")
      .headers(CommonHeader)
      .body(StringBody("{\n  \"data\": {\n    \"caseReference\": null,\n    \"caseCreated\": \"2020-02-11\",\n    \"region\": \"London\",\n    \"appeal\": {\n      \"receivedVia\": \"Online\",\n      \"mrnDetails\": {\n        \"dwpIssuingOffice\": null,\n        \"mrnDate\": null,\n        \"mrnLateReason\": \"Test\",\n        \"mrnMissingReason\": null\n      },\n      \"appellant\": {\n        \"name\": {\n          \"title\": \"Mr\",\n          \"firstName\": \"Dan\",\n          \"middleName\": null,\n          \"lastName\": \"Gleeballs\"\n        },\n        \"identity\": {\n          \"dob\": \"1970-10-02\",\n          \"nino\": \"ab123456z\"\n        },\n        \"address\": {\n          \"line1\": \"5 test street\",\n          \"line2\": null,\n          \"line3\": null,\n          \"town\": \"london\",\n          \"county\": null,\n          \"postcode\": \"kt25bu\",\n          \"country\": \"uk\"\n        },\n        \"contact\": {\n          \"phone\": \"07123456789\",\n          \"mobile\": null,\n          \"email\": \"test@test.com\"\n        },\n        \"isAppointee\": null,\n        \"appointee\": {\n          \"name\": {\n            \"title\": null,\n            \"firstName\": null,\n            \"middleName\": null,\n            \"lastName\": null\n          },\n          \"identity\": {\n            \"dob\": null,\n            \"nino\": null\n          },\n          \"address\": {\n            \"line1\": null,\n            \"line2\": null,\n            \"line3\": null,\n            \"town\": null,\n            \"county\": null,\n            \"postcode\": null,\n            \"country\": null\n          },\n          \"contact\": {\n            \"phone\": null,\n            \"mobile\": null,\n            \"email\": null\n          }\n        },\n        \"isAddressSameAsAppointee\": null\n      },\n      \"benefitType\": {\n        \"code\": \"DWP\",\n        \"description\": null\n      },\n      \"hearingType\": \"cor\",\n      \"hearingOptions\": {\n        \"wantsToAttend\": \"No\",\n        \"languageInterpreter\": \"No\",\n        \"other\": null,\n        \"signLanguageType\": null\n      },\n      \"appealReasons\": {\n        \"reasons\": [],\n        \"otherReasons\": null\n      },\n      \"supporter\": {\n        \"name\": {\n          \"title\": null,\n          \"firstName\": null,\n          \"middleName\": null,\n          \"lastName\": null\n        },\n        \"contact\": {\n          \"phone\": null,\n          \"mobile\": null,\n          \"email\": null\n        }\n      },\n      \"rep\": {\n        \"hasRepresentative\": null\n      },\n      \"signer\": null\n    },\n    \"regionalProcessingCenter\": {\n      \"name\": null,\n      \"address1\": null,\n      \"address2\": null,\n      \"address3\": null,\n      \"address4\": null,\n      \"postcode\": null,\n      \"city\": null,\n      \"phoneNumber\": null,\n      \"faxNumber\": null\n    },\n    \"panel\": {\n      \"assignedTo\": null,\n      \"medicalMember\": null,\n      \"disabilityQualifiedMember\": null\n    }\n  },\n  \"event\": {\n    \"id\": \"appealCreated\",\n    \"summary\": \"test create case\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${New_Case_event_token}\",\n  \"ignore_warning\": false,\n  \"draft_id\": null\n}"))
      .check(jsonPath("$.id").saveAs("New_Case_Id")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val PrintCaseID = exec {
    session =>
      println(session("New_Case_Id").as[String])
      session
  }

  val SSCSDocUpload = group("SSCS_DocUpload") {

//    exec(http("SSCS_040_005_DocumentUpload")
//      .get("/data/internal/cases/${New_Case_Id}/event-triggers/attachScannedDocs?ignore-warning=false")
//      .headers(headers_9)
//      .check(jsonPath("$.event_token").saveAs("existing_case_event_token")))

    exec(http("SSCS_040_005_DocumentUpload")
      .get("/data/internal/cases/${New_Case_Id}/event-triggers/uploadDocument?ignore-warning=false")
      .headers(headers_9)
      .check(jsonPath("$.event_token").saveAs("existing_case_event_token")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("SSCS_040_010_DocumentUpload")
      .post(BaseURL + "/documents")
      .bodyPart(RawFileBodyPart("files", "1MB.pdf")
        .fileName("1MB.pdf")
        .transferEncoding("binary"))
      .asMultipartForm
      .formParam("classification", "PUBLIC")
      .check(status.is(200))
      .check(regex("""http://(.+)/""").saveAs("DMURL"))
      .check(regex("""/documents/(.+)"""").saveAs("Document_ID")))

    .exec(http("SSCS_040_015_DocumentUpload")
      .post("/data/caseworkers/:uid/jurisdictions/SSCS/case-types/Benefit/cases/${New_Case_Id}/events")
      .headers(CommonHeader)
      .body(StringBody("{\n  \"data\": {\n    \"sscsDocument\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"documentType\": \"Other evidence\",\n          \"documentEmailContent\": null,\n          \"documentDateAdded\": \"2020-02-11\",\n          \"documentComment\": \"test upload\",\n          \"documentFileName\": null,\n          \"documentLink\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal:443/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          }\n        }\n      },\n      {\n        \"id\": null,\n        \"value\": {\n          \"documentType\": null,\n          \"documentEmailContent\": null,\n          \"documentDateAdded\": null,\n          \"documentComment\": null,\n          \"documentFileName\": null\n        }\n      }\n    ]\n  },\n  \"event\": {\n    \"id\": \"uploadDocument\",\n    \"summary\": \"test upload doc\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false\n}")))

//    .exec(http("SSCS_040_015_DocumentUpload")
//      .post("/data/case-types/${SSCSCaseType}/validate?pageId=attachScannedDocs1.0")
//      .headers(headers_22)
//      .body(StringBody("{\n  \"data\": {\n    \"scannedDocuments\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"type\": \"other\",\n          \"url\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          },\n          \"controlNumber\": \"1234\",\n          \"fileName\": \"1MB.pdf\",\n          \"scannedDate\": \"2020-02-10T00:00:00.000\",\n          \"exceptionRecordReference\": \"test\",\n          \"subtype\": \"evidence\",\n          \"deliveryDate\": \"2020-02-10T00:00:00.000\"\n        }\n      }\n    ]\n  },\n  \"event\": {\n    \"id\": \"attachScannedDocs\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false,\n  \"event_data\": {\n    \"scannedDocuments\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"type\": \"other\",\n          \"url\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          },\n          \"controlNumber\": \"1234\",\n          \"fileName\": \"1MB.pdf\",\n          \"scannedDate\": \"2020-02-10T00:00:00.000\",\n          \"exceptionRecordReference\": \"test\",\n          \"subtype\": \"evidence\",\n          \"deliveryDate\": \"2020-02-10T00:00:00.000\"\n        }\n      }\n    ]\n  },\n  \"case_reference\": \"${New_Case_Id}\"\n}")))
//
//    .exec(http("SSCS_040_020_DocumentUpload")
//      .post("/data/case-types/${SSCSCaseType}/validate?pageId=attachScannedDocs2.0")
//      .headers(headers_22)
//      .body(StringBody("{\n  \"data\": {\n    \"evidenceHandled\": \"No\"\n  },\n  \"event\": {\n    \"id\": \"attachScannedDocs\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false,\n  \"event_data\": {\n    \"scannedDocuments\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"type\": \"other\",\n          \"url\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          },\n          \"controlNumber\": \"1234\",\n          \"fileName\": \"1MB.pdf\",\n          \"scannedDate\": \"2020-02-10T00:00:00.000\",\n          \"exceptionRecordReference\": \"test\",\n          \"subtype\": \"evidence\",\n          \"deliveryDate\": \"2020-02-10T00:00:00.000\"\n        }\n      }\n    ],\n    \"evidenceHandled\": \"No\"\n  },\n  \"case_reference\": \"${New_Case_Id}\"\n}")))
//
//    .exec(http("SSCS_040_025_DocumentUpload")
//      .post("/data/cases/${New_Case_Id}/events")
//      .headers(headers_28)
//      .body(StringBody("{\n  \"data\": {\n    \"scannedDocuments\": [\n      {\n        \"id\": null,\n        \"value\": {\n          \"type\": \"other\",\n          \"url\": {\n            \"document_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\n            \"document_binary_url\": \"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\n            \"document_filename\": \"1MB.pdf\"\n          },\n          \"controlNumber\": \"1234\",\n          \"fileName\": \"1MB.pdf\",\n          \"scannedDate\": \"2020-02-10T00:00:00.000\",\n          \"exceptionRecordReference\": \"test\",\n          \"subtype\": \"evidence\",\n          \"deliveryDate\": \"2020-02-10T00:00:00.000\"\n        }\n      }\n    ],\n    \"evidenceHandled\": \"No\"\n  },\n  \"event\": {\n    \"id\": \"attachScannedDocs\",\n    \"summary\": \"\",\n    \"description\": \"\"\n  },\n  \"event_token\": \"${existing_case_event_token}\",\n  \"ignore_warning\": false\n}")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val SSCSSearchAndView = group("SSCS_View") {
    exec(http("SSCS_050_005_SearchPage")
      .get("/data/internal/case-types/${SSCSCaseType}/work-basket-inputs")
      .headers(headers_0))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("SSCS_050_010_SearchAndView")
      .get("/aggregated/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/cases?view=WORKBASKET&page=1&case_reference=${New_Case_Id}")
      .headers(CommonHeader))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("SSCS_050_015_SearchAndView")
      .get("/data/caseworkers/:uid/jurisdictions/${SSCSJurisdiction}/case-types/${SSCSCaseType}/cases/pagination_metadata?case_reference=${New_Case_Id}")
      .headers(CommonHeader))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("SSCS_050_020_SearchAndView")
      .get("/data/internal/cases/${New_Case_Id}")
      .headers(headers_8))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .exec(http("SSCS_050_025_SearchAndOpenDoc")
      .get("/documents/${Document_ID}/binary")
      .headers(headers_15))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }
}


