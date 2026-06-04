package com.example.model

object TaskRepository {

    val fires = mutableListOf(
        Fire("Seven Cabins", 31770, County.Lincoln),
        Fire("Summit Creek Fire", 1743, County.Cassia),
        Fire("Murphy Fire", 20, County.Albany),
    )
    fun allFires(): List<Fire> = fires.toList()

    fun firesByCounty(county: County) = fires.filter {
        it.county == county
    }

    fun firesByCounties(counties: List<String?>) = fires.filter {
        it.county.toString() == counties[0] || it.county.toString() == counties[1]
    }

    fun fireByName(name: String) = fires.find {
        it.name.equals(name, ignoreCase = true)
    }

    fun addFire(fire: Fire) {
        if(fireByName(fire.name) != null) {
            throw IllegalStateException("Cannot duplicate fire names!")
        }
        fires.add(fire)
    }
}