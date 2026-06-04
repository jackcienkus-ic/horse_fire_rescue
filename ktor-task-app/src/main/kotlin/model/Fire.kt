package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class Fire(
    val name: String,
    val size: Double?,
    val lng: Double?,
    val lat: Double?
)

fun Fire.fireAsRow() = """
    <tr>
        <td>$name</td><td>$size</td><td>$lat</td><td>$lng</td>
    </tr>
    """.trimIndent()

fun List<Fire>.fireAsTable() = this.joinToString(
    prefix = "<table rules=\"all\"><tr><th>Name</th><th>Size</th><th>Lat</th><th>Lng</th></tr>",
    postfix = "</table>",
    separator = "\n",
    transform = Fire::fireAsRow
)