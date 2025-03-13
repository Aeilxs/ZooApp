package com.isen.zooapp

data class Biome(
    val id: String = "",
    val color: String = "",
    val name: String = "",
    val enclosures: List<Enclosure> = emptyList()
)
