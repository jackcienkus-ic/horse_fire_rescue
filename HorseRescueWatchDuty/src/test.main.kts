@file:DependsOn("org.postgresql:postgresql:42.7.3")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

import kotlinx.serialization.json.*
import java.sql.DriverManager

fun main() {
    val url  = "jdbc:postgresql://localhost:5432/postgres"
    val user = "jackcienkus"
    val pass = ""

    val connection = java.net.URL("https://inciweb.wildfire.gov/api/map_data")
        .openConnection() as java.net.HttpURLConnection
    connection.setRequestProperty("Accept", "application/json")
    connection.setRequestProperty("User-Agent", "Mozilla/5.0")
    val response = connection.inputStream.bufferedReader().readText()

    val features = Json.parseToJsonElement(response).jsonArray

    DriverManager.getConnection(url, user, pass).use { conn ->
        val sql = """
            INSERT INTO fires (id, name, lat, lng, date_created, type)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (id) DO NOTHING
        """
        conn.prepareStatement(sql).use { stmt ->
            var count = 0
            for (feature in features) {
                try {
                    val f = feature.jsonObject

                    val id          = f["id"]?.jsonPrimitive?.int ?: continue
                    val name        = f["title"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
                    val lat         = f["lat_deg"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                    val lng         = f["long_deg"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                    val dateCreated = f["created"]?.jsonPrimitive?.contentOrNull
                    val type        = f["type"]?.jsonPrimitive?.contentOrNull

                    stmt.setInt(1, id)
                    stmt.setString(2, name)
                    stmt.setDouble(3, lat)
                    stmt.setDouble(4, lng)
                    stmt.setString(5, dateCreated)
                    stmt.setString(6, type)
                    stmt.executeUpdate()
                    count++
                } catch (e: Exception) {
                    println("Skipped row: ${e.message}")
                }
            }
            println("Inserted $count fires.")
        }
    }
}

main()