package com.example.pantrypals.data

data class Preferences(
    val useProteinGoal: Boolean = false,
    val protein: String = "",
    val useCaloriesGoal: Boolean = false,
    val calories: String = "",
    val useCarbsGoal: Boolean = false,
    val carbs: String = "",
    val selectedProteins: List<String> = emptyList(),
    val dietaryRestrictions: String = "",
    val favoriteIngredients: String = "",
    val dislikes: String = ""
)