package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.freemarker.*
import kotlinx.serialization.json.*
import java.util.LinkedList

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }
        get("/fires"){
            val dataUrl = java.net.URL("https://services3.arcgis.com/T4QMspbfLg3qTGWY/arcgis/rest/services/WFIGS_Incident_Locations_Current/FeatureServer/0/query?outFields=*&where=1%3D1&f=geojson")
            val response = dataUrl.readText()
            val root = Json.parseToJsonElement(response).jsonObject
            val features = root["features"]!!.jsonArray
//    val data = mutableListOf<Fire>()
            val linkedList = LinkedList<Fire>()
            for (feature in features) {
                val props = feature.jsonObject["properties"]!!.jsonObject
                val coords = feature.jsonObject["geometry"]?.jsonObject
                    ?.get("coordinates")?.jsonArray

                val name = props["IncidentName"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
                val lng  = coords?.get(0)?.jsonPrimitive?.double ?: 0.0
                val lat  = coords?.get(1)?.jsonPrimitive?.double ?: 0.0

                val currentFire = Fire(name, lat, lng)
                linkedList.add(currentFire)
            }
            call.respond(FreeMarkerContent("fires.ftl", mapOf("data" to linkedList), ""))
//            call.respond(FreeMarkerContent("fires.ftl", mapOf("data" to Fires(listOf("Seven Cabins", "Jefferson", "Trailhead Prescribed"), listOf(1.1, 2.2, 3.3), listOf(4.4, 5.5, 6.6))), ""))
        }
    }
}