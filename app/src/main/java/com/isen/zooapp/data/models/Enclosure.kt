package com.isen.zooapp.data.models

data class Enclosure(
    val id: String = "",
    val id_biomes: String = "",
    val meal: String = "",
    val animals: List<Animal> = emptyList()
)
