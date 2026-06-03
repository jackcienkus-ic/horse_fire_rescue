@file:DependsOn("org.postgresql:postgresql:42.7.3")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

import kotlinx.serialization.json.*
import java.sql.DriverManager

fun main() {
    val url  = "jdbc:postgresql://localhost:5432/postgres"
    val user = "jackcienkus"
    val pass = ""

    val dataUrl = java.net.URL("https://inciweb.wildfire.gov/api/util/all_select_data")
    val response = dataUrl.readText()
    val features = Json.parseToJsonElement(response).jsonArray

    DriverManager.getConnection(url, user, pass).use { conn ->
        val sql = """
            INSERT INTO fires (id, name, lat, lng, date_created, is_active)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (id) DO NOTHING
        """
        conn.prepareStatement(sql).use { stmt ->
            var count = 0
            for (feature in features) {
                try {
                    val f = feature.jsonObject

                    val id          = f["id"]?.jsonPrimitive?.int ?: continue
                    val name        = f["name"]?.jsonPrimitive?.contentOrNull ?: "Unknown"
                    val lat         = f["lat"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                    val lng         = f["lng"]?.jsonPrimitive?.doubleOrNull ?: 0.0
                    val dateCreated = f["date_created"]?.jsonPrimitive?.contentOrNull
                    val isActive    = f["is_active"]?.jsonPrimitive?.boolean ?: false

                    stmt.setInt(1, id)
                    stmt.setString(2, name)
                    stmt.setDouble(3, lat)
                    stmt.setDouble(4, lng)
                    stmt.setString(5, dateCreated)
                    stmt.setBoolean(6, isActive)
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