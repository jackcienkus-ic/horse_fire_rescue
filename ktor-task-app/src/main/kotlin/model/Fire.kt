package com.example.model

import kotlinx.serialization.Serializable


@Serializable
data class Fire(
    val name: String,
    val size: Double?,
    val county: String
)

fun Fire.fireAsRow() = """
    <tr>
        <td>$name</td><td>$size</td><td>$county</td>
    </tr>
    """.trimIndent()

fun List<Fire>.fireAsTable() = this.joinToString(
    prefix = "<table rules=\"all\">",
    postfix = "</table>",
    separator = "\n",
    transform = Fire::fireAsRow
)