package com.example.pantrypals

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Preferences : Screen("preferences")
    object SavedMeals : Screen("saved_meals")
}
