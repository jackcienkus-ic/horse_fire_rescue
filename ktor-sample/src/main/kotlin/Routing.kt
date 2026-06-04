package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.freemarker.*
import kotlinx.serialization.json.*
import java.util.LinkedList
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.math.cos

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }
        get("/fires"){
//            val dataUrl = java.net.URL("https://services3.arcgis.com/T4QMspbfLg3qTGWY/arcgis/rest/services/WFIGS_Incident_Locations_Current/FeatureServer/0/query?outFields=*&where=1%3D1&f=geojson")
//            val response = dataUrl.readText()
//            val root = Json.parseToJsonElement(response).jsonObject
//            val features = root["features"]!!.jsonArray
            val client = HttpClient.newHttpClient()

            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.watchduty.org/api/v1/geo_events/?geo_event_types=wildfire,location&ts=1780418777000"))
                .GET()
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("accept-language", "en-US,en;q=0.9")
                .header("cache-control", "max-age=0")
                .header("sec-ch-ua", "\"Chromium\";v=\"148\", \"Google Chrome\";v=\"148\", \"Not/A)Brand\";v=\"99\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "document")
                .header("sec-fetch-mode", "navigate")
                .header("sec-fetch-site", "cross-site")
                .header("sec-fetch-user", "?1")
                .header("upgrade-insecure-requests", "1")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36")
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val body = response.body() ?: error("Empty response")

            val features = Json { ignoreUnknownKeys = true }
                .parseToJsonElement(body).jsonArray
            val region = call.request.queryParameters["region"] ?: "0.0"
            val linkedList = LinkedList<Fire>()
            val mileDiff = region.toDoubleOrNull() ?: return@get call.respondText(
                "Missing or invalid parameter. Must be a valid Double."
            )

            for (feature in features) {
                val f = feature.jsonObject
                val data = f.jsonObject["data"]!!.jsonObject
                val name = f["name"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
                val size = (data["acreage"] as? JsonPrimitive)?.doubleOrNull
                val lat  = f["lat"]?.jsonPrimitive?.double ?: 0.0
                val lng  = f["lng"]?.jsonPrimitive?.double ?: 0.0
                if (lat in (36.99 - (mileDiff/69.1))..(41.00 + (mileDiff/69.1))
                    && lng in (-109.05 - (mileDiff/(69.17*(cos(lat)))))..(-102.05 + (mileDiff/(69.17*(cos(lat)))))) {
                    val currentFire = Fire(name, size, lat, lng)
                    linkedList.add(currentFire)
                }
            }
            call.respond(FreeMarkerContent("fires.ftl", mapOf("data" to linkedList, "region" to region), ""))
        }
    }
}