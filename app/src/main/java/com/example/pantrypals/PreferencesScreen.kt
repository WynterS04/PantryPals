package com.example.pantrypals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pantrypals.viewmodel.PreferencesViewModel
import com.example.pantrypals.data.Preferences
@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel = viewModel(),
    onGenerate: (Preferences) -> Unit = {}
) {
    val prefs = viewModel.preferences.value

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Text(
                text = "Meal Preferences",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center ,
                color = Color(0xFFD16B2F),
                style = MaterialTheme.typography.headlineLarge
            )
        }

        // PROTEIN GOAL
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = prefs.useProteinGoal,
                    onCheckedChange = { viewModel.toggleProteinGoal(it) }
                )

                Text("Protein", modifier = Modifier.width(80.dp))

                TextField(
                    value = prefs.protein,
                    onValueChange = { viewModel.updateProtein(it) },
                    enabled = prefs.useProteinGoal,
                    modifier = Modifier.width(120.dp)
                )
            }
        }

        // CALORIES
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = prefs.useCaloriesGoal,
                    onCheckedChange = { viewModel.toggleCaloriesGoal(it) }
                )

                Text("Calories", modifier = Modifier.width(80.dp))

                TextField(
                    value = prefs.calories,
                    onValueChange = { viewModel.updateCalories(it) },
                    enabled = prefs.useCaloriesGoal
                )
            }
        }

        // CARBS
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = prefs.useCarbsGoal,
                    onCheckedChange = { viewModel.toggleCarbsGoal(it) }
                )

                Text("Carbs", modifier = Modifier.width(80.dp))

                TextField(
                    value = prefs.carbs,
                    onValueChange = { viewModel.updateCarbs(it) },
                    enabled = prefs.useCarbsGoal
                )
            }
        }

        item {
            Text("Choose preferred proteins:")
        }

        // PROTEIN OPTIONS
        items(listOf("Chicken", "Beef", "Pork", "Fish", "Tofu/Vegan")) { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = prefs.selectedProteins.contains(option),
                    onCheckedChange = {
                        val updated = prefs.selectedProteins.toMutableList()
                        if (it) updated.add(option) else updated.remove(option)
                        viewModel.updateSelectedProteins(updated)
                    }
                )
                Text(option)
            }
        }

        // DIETARY RESTRICTIONS
        item {
            TextField(
                value = prefs.dietaryRestrictions,
                onValueChange = { viewModel.updateDiet(it) },
                label = { Text("Dietary Restrictions / Allergies") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // FAVORITES
        item {
            TextField(
                value = prefs.favoriteIngredients,
                onValueChange = { viewModel.updateFavIngredients(it) },
                label = { Text("Favorite Ingredients") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // DISLIKES
        item {
            TextField(
                value = prefs.dislikes,
                onValueChange = { viewModel.updateDislikes(it) },
                label = { Text("Dislikes") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // BUTTON
        item {
            Button(
                onClick = { onGenerate(prefs) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Generate Recipe",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
