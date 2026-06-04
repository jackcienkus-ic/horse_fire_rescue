@file:DependsOn("org.postgresql:postgresql:42.7.3")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

import kotlinx.serialization.json.*
import java.sql.DriverManager

data class Fire(val name: String, val lat: Double, val lng: Double)
data class Node(var fire: Fire? = null, var next: Node? = null)
data class LinkedList(var head: Node? = null, var size: Int = 0)

fun main()  {
    val dataUrl = java.net.URL("https://services3.arcgis.com/T4QMspbfLg3qTGWY/arcgis/rest/services/WFIGS_Incident_Locations_Current/FeatureServer/0/query?outFields=*&where=1%3D1&f=geojson")
    val response = dataUrl.readText()
    val root = Json.parseToJsonElement(response).jsonObject
    val features = root["features"]!!.jsonArray
//    val data = mutableListOf<Fire>()
    val linkedList = LinkedList()
    var currentNode = linkedList.head
    for (feature in features) {
        val props = feature.jsonObject["properties"]!!.jsonObject
        val coords = feature.jsonObject["geometry"]?.jsonObject
            ?.get("coordinates")?.jsonArray

        val name = props["IncidentName"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
        val lng  = coords?.get(0)?.jsonPrimitive?.double ?: 0.0
        val lat  = coords?.get(1)?.jsonPrimitive?.double ?: 0.0

        val currentFire = Fire(name, lat, lng)
        val nextNode = Node(currentFire)
        if (linkedList.head == null) {
            currentNode = Node(currentFire)
            linkedList.head = currentNode
        }
        else {
            currentNode?.next = nextNode
            currentNode = nextNode
        }
        linkedList.size++
    }
    var i = 0
    currentNode = linkedList.head
    while (i < linkedList.size && currentNode != null) {
        println("Name: " + currentNode.fire?.name + " Lat: " + currentNode.fire?.lat + " Lng: " + currentNode.fire?.lng)
        currentNode = currentNode.next
        i++
    }
}

//fun main() {
//    val url  = "jdbc:postgresql://localhost:5432/postgres"
//    val user = "jackcienkus"
//    val pass = ""
//
//    val connection = java.net.URL("https://api.watchduty.org/api/v1/geo_events/?geo_event_types=wildfire,location&ts=1780418777000")
//        .openConnection() as java.net.HttpURLConnection
//    val response = connection.inputStream.bufferedReader().readText()
//    val features = Json.parseToJsonElement(response).jsonArray
//
//    DriverManager.getConnection(url, user, pass).use { conn ->
//        val sql = """
//            INSERT INTO fires (id, name, lat, lng, date_created, is_active)
//            VALUES (?, ?, ?, ?, ?, ?)
//            ON CONFLICT (id) DO NOTHING
//        """
//        conn.prepareStatement(sql).use { stmt ->
//            var count = 0
//            for (feature in features) {
//                try {
//                    val f = feature.jsonObject
//
//                    val id          = f["id"]?.jsonPrimitive?.int ?: continue
//                    val name        = f["name"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
//                    val lat         = f["lat"]?.jsonPrimitive?.doubleOrNull ?: 0.0
//                    val lng         = f["lng"]?.jsonPrimitive?.doubleOrNull ?: 0.0
//                    val dateCreated = f["date_created"]?.jsonPrimitive?.contentOrNull
//                    val isActive    = f["is_active"]?.jsonPrimitive?.boolean ?: false
//
//                    stmt.setInt(1, id)
//                    stmt.setString(2, name)
//                    stmt.setDouble(3, lat)
//                    stmt.setDouble(4, lng)
//                    stmt.setString(5, dateCreated)
//                    stmt.setBoolean(6, isActive)
//                    stmt.executeUpdate()
//                    count++
//                } catch (e: Exception) {
//                    println("Skipped row: ${e.message}")
//                }
//            }
//            println("Inserted $count fires.")
//        }
//    }
//}

main()