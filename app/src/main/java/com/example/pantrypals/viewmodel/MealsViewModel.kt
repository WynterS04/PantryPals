package com.example.pantrypals.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.pantrypals.data.Meal

class MealsViewModel : ViewModel() {

    private var nextId = 0

    var meals = mutableStateListOf<Meal>()
        private set

    fun addMeal(text: String?) {
        if (text == null) return

        meals.add(
            Meal(
                id = nextId++,
                text = text
            )
        )
    }

    fun toggleFavorite(meal: Meal) {
        val index = meals.indexOf(meal)
        if (index != -1) {
            meals[index] = meals[index].copy(
                isFavorite = !meal.isFavorite
            )
        }
    }
}