package com.example

import freemarker.cache.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*

data class IndexData(val items: List<Int>)
//data class Fires(val fires: List<String>, val lat: List<Double>, val lng: List<Double>)
data class Fire(val name: String, val size: Double?, val lat: Double, val lng: Double)
//data class BoundingBox(val minLat: Double, val maxLat: Double, val minLng: Double, val maxLng: Double)
//
//val boundingBoxes = mapOf(
//    "colorado"  to BoundingBox(36.99, 41.00, -109.05, -102.05),
//    "southwest" to BoundingBox(31.33, 42.00, -114.82, -102.05),
//    "west"      to BoundingBox(31.33, 49.00, -124.73, -102.05),
//    "all"       to BoundingBox(24.00, 49.38, -125.00, -66.93)
//)
