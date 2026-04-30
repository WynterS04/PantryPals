package com.example.pantrypals.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.pantrypals.data.Meal

class MealsViewModel : ViewModel() {

    val meals = mutableStateListOf<Meal>()

    fun toggleMeal(meal: Meal) {
        val exists = meals.any { it.text == meal.text }

        if (exists) {
            meals.removeAll { it.text == meal.text }
        } else {
            meals.add(meal)
        }
    }

    fun isMealSaved(recipeText: String): Boolean {
        return meals.any { it.text == recipeText }
    }
}