package com.example

import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.*
import com.example.model.FireRepository.getFires


fun Application.configureRouting() {
    routing {
        get ("/") {
            call.respondText("Hello World!")
        }
        get("/fires") {
            val fires = FireRepository.allFires("https://api.watchduty.org/api/v1/geo_events/?geo_event_types=wildfire,location")
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = fires.fireAsTable()
            )
        }

        get("/fires/colorado") {
            val fires = FireRepository.getFires("https://api.watchduty.org/api/v1/geo_events/?geo_event_types=wildfire,location", 1.0)
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = fires.fireAsTable()
            )
        }

        get("/fires/colorado//boundaryDegree/{degrees}") {
            val boundary = call.parameters["degrees"]!!.toDouble()
            val fires = FireRepository.getFires("https://api.watchduty.org/api/v1/geo_events/?geo_event_types=wildfire,location", boundary)
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = fires.fireAsTable()
            )
        }

        get("/fires/colorado//boundaryMile/{miles}") {
            val boundary = (call.parameters["miles"]!!.toDouble())/50
            val fires = FireRepository.getFires("https://api.watchduty.org/api/v1/geo_events/?geo_event_types=wildfire,location", boundary)
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = fires.fireAsTable()
            )
        }

        get("/test/jsonread") {
            val fireListDisplay = getFires("https://api.watchduty.org/api/v1/geo_events/?geo_event_types=wildfire,location", 1.0)
            val textToDisplay=fireListDisplay.toString()
            call.respondText(textToDisplay)
        }
    }
}