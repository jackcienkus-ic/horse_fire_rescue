package com.example.model

import java.net.URL
import kotlinx.serialization.json.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


object FireRepository {

    fun allFires(url: String): List<Fire> = getFires(url, 100.0).toList()

    fun getFires(url: String, boundary: Double): List<Fire> {
        val fireList: MutableList<Fire> = mutableListOf()

        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36")
            .header("Accept-Language", "en-US,en;q=0.9")
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val body = response.body() ?: error("Empty Response")
        val features = Json.parseToJsonElement(body).jsonArray

        for (feature in features) {
            val f = feature.jsonObject
            val name = f["name"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
            val data = f.jsonObject["data"]!!.jsonObject
            val size = (data["acreage"] as? JsonPrimitive)?.doubleOrNull
            val longitude = f["lng"]?.jsonPrimitive?.double ?: 0.0
            val latitude = f["lat"]?.jsonPrimitive?.double ?: 0.0

            if (latitude in (37.0-boundary)..(41.0+boundary) && longitude in (-109.046667-boundary)..(-102.046667+boundary)){
                val newFireInfo = Fire(name, size, longitude, latitude)
                fireList.add(newFireInfo)
            }



        }
        return fireList
    }

}