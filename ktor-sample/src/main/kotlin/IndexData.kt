package com.example

import freemarker.cache.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*

data class IndexData(val items: List<Int>)
data class Fires(val fires: List<String>, val lat: List<Double>, val lng: List<Double>)