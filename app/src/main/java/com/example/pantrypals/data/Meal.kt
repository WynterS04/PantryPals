package com.example.pantrypals.data

data class Meal(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isFavorite: Boolean = false
)