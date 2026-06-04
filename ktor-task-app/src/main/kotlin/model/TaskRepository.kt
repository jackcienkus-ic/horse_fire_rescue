package com.example.model

import java.net.URL
import kotlinx.serialization.json.*



object TaskRepository {

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
            val newFireInfo = Fire(name, size, county)
            fireList.add(newFireInfo)
            println(newFireInfo)
        }
        print(fireList)
        return fireList
    }

}