package com.example.model

enum class County {
    Jefferson, Cassia, Lincoln, Converse, Albany
}

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