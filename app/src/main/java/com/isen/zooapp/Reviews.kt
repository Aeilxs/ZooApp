package com.isen.zooapp

data class Review(
    val userId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val timestamp: Long = 0L
)
