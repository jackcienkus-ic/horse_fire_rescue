package com.example.model

import java.net.URL
import kotlinx.serialization.json.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


object FireRepository {

    val fires = getFires("https://services3.arcgis.com/T4QMspbfLg3qTGWY/arcgis/rest/services/WFIGS_Incident_Locations_Current/FeatureServer/0/query?outFields=*&where=1%3D1&f=geojson")
    fun allFires(): List<Fire> = fires.toList()

    fun firesByCounty(county: String) = fires.filter {
        it.county == county
    }

    fun firesByCounties(counties: List<String?>) = fires.filter {
        it.county == counties[0] || it.county == counties[1]
    }

    fun fireByName(name: String) = fires.find {
        it.name.equals(name, ignoreCase = true)
    }


    fun getFires(url: String): List<Fire> {
        val fireList: MutableList<Fire> = mutableListOf()

        """val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .header("Content-Type", "application/json")
            .build()"""

        val rawJson: String = URL(url).readText()
        val element = Json.parseToJsonElement(rawJson).jsonObject
        val features = element["features"]!!.jsonArray
        for (feature in features) {
            val props = feature.jsonObject["properties"]!!.jsonObject
            val coords = feature.jsonObject["geometry"]?.jsonObject
                ?.get("coordinates")?.jsonArray
            val name = props["IncidentName"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
            val size = props["IncidentSize"]?.jsonPrimitive?.doubleOrNull
            val county = props["POOCounty"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
            val longitude = coords?.get(0)?.jsonPrimitive?.double ?: 0.0
            val latitude = coords?.get(1)?.jsonPrimitive?.double ?: 0.0
            val newFireInfo = Fire(name, size, county, longitude, latitude)
            fireList.add(newFireInfo)
        }
        return fireList
    }

}