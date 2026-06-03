package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.freemarker.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }
        get("/fires"){
            call.respond(FreeMarkerContent("fires.ftl", mapOf("data" to FireNames(listOf("Seven Cabins", "Jefferson", "Trailhead Prescribed"))), ""))
        }
    }
}