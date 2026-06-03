package com.example

import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.*
import io.ktor.http.HttpStatusCode


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

        get("/fires/byCounty/{county?}") {
            val countyAsText = call.parameters["county"]
            if (countyAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val county = County.valueOf(countyAsText)
                val fires = TaskRepository.firesByCounty(county)

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