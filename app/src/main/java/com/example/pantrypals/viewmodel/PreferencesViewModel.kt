package com.example.pantrypals.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pantrypals.data.Preferences

class PreferencesViewModel : ViewModel() {

    var preferences = mutableStateOf(Preferences())
        private set

    fun updateDiet(value: String) {
        preferences.value = preferences.value.copy(dietaryRestrictions = value)
    }

    fun updateFavIngredients(value: String) {
        preferences.value = preferences.value.copy(favoriteIngredients = value)
    }

    fun updateDislikes(value: String) {
        preferences.value = preferences.value.copy(dislikes = value)
    }

    fun toggleProteinGoal(enabled: Boolean) {
        preferences.value = preferences.value.copy(useProteinGoal = enabled)
    }
    fun updateProtein(value: String) {
        preferences.value = preferences.value.copy(protein = value)
    }

    fun toggleCaloriesGoal(enabled: Boolean) {
        preferences.value = preferences.value.copy(useCaloriesGoal = enabled)
    }

    fun updateCalories(value: String) {
        preferences.value = preferences.value.copy(calories = value)
    }

    fun toggleCarbsGoal(enabled: Boolean) {
        preferences.value = preferences.value.copy(useCarbsGoal = enabled)
    }

    fun updateCarbs(value: String) {
        preferences.value = preferences.value.copy(carbs = value)
    }

    fun updateSelectedProteins(list: List<String>) {
        preferences.value = preferences.value.copy(selectedProteins = list)
    }
}