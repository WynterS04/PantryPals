package com.example.pantrypals.data

data class Meal(
    val id: Int,
    val text: String,
    val isFavorite: Boolean = false
)