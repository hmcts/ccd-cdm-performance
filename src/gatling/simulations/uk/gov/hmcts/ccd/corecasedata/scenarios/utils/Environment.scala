package uk.gov.hmcts.ccd.corecasedata.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config.ConfigFactory

object Environment {

  val idamURL = "https://idam-web-public.aat.platform.hmcts.net"
  val idamAPI = "https://idam-api.aat.platform.hmcts.net"
  val ccdEnvurl = "https://www-ccd.aat.platform.hmcts.net"
  val baseURL = "https://gateway-ccd.aat.platform.hmcts.net"
  val s2sUrl = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal"
  val ccdDataStoreUrl = "http://ccd-data-store-api-aat.service.core-compute-aat.internal"
  val ccdGatewayCS = ConfigFactory.load.getString("auth.ccdGatewayCS")

  val minThinkTime = 1 //10
  val maxThinkTime = 2 //20
  val constantthinkTime = 7
  val minWaitForNextIteration = 120 //120
  val maxWaitForNextIteration = 240 //240
  val HttpProtocol = http

  val commonHeader = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> ccdEnvurl)

  val docCommonHeader = Map(
    "Content-Type" -> "application/pdf",
    "Origin" -> ccdEnvurl)

  val idam_header = Map(
    "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Origin" -> idamURL,
    "Upgrade-Insecure-Requests" -> "1",
    "sec-fetch-dest" -> "document",
    "sec-fetch-mode" -> "navigate",
    "sec-fetch-site" -> "same-origin",
    "sec-fetch-user" -> "?1",
    "cache-control" -> "max-age=0")
}
