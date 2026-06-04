package com.example

import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.*
import io.ktor.http.HttpStatusCode
import com.example.model.TaskRepository.getFires


fun Application.configureRouting() {
    routing {
        get ("/") {
            call.respondText("Hello World!")
        }
        get("/fires") {
            val fires = TaskRepository.allFires()
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = fires.fireAsTable()
            )
        }

        get("/fires/{countyA}/{countyB}"){
            val ca=call.parameters["countyA"]
            val cb=call.parameters["countyB"]
            val tags: List<String?> = listOf(ca, cb)
            val fires=TaskRepository.firesByCounties(tags)
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = fires.fireAsTable()
            )
        }

        get("/test/jsonread") {            val fireListDisplay = getFires("https://services3.arcgis.com/T4QMspbfLg3qTGWY/arcgis/rest/services/WFIGS_Incident_Locations_Current/FeatureServer/0/query?outFields=*&where=1%3D1&f=geojson")

            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = fireListDisplay.fireAsTable()
            )
        }

        get("/fires/byCounty/{county?}") {
            val countyAsText = call.parameters["county"]
            if (countyAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val fires = TaskRepository.firesByCounty(countyAsText)

                if (fires.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = fires.fireAsTable()
                )
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}