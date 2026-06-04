package com.example.model

import kotlinx.serialization.Serializable


enum class County {
    Jefferson, Cassia, Lincoln, Converse, Albany
}

@Serializable
data class Fire(
    val name: String,
    val size: Int,
    val county: County
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