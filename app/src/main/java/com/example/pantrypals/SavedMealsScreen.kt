package com.example.pantrypals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Meal(
    val id: Int,
    val name: String,
    val recipe: String,
    val time: String,      // e.g., "20 min"
    val servings: String,  // e.g., "2 servings"
    val calories: String   // e.g., "450 kcal"
)

@Composable
fun SavedMealsScreen() {
    // WILL CHANGE THESE WHEN WE HAVE DYNAMIC DATA
    val savedMeals = remember {
        mutableStateListOf(
            Meal(1, "Pesto Pasta", "Boil pasta...", "15 min", "2 servings", "500 kcal"),
            Meal(2, "Chickpea Salad", "Mix veggies...", "10 min", "1 serving", "320 kcal")
        )
    }

    // State to track which recipe is currently being viewed
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(savedMeals) { meal ->
                MealItem(
                    meal = meal,
                    onDelete = { savedMeals.remove(meal) },
                    onClick = { selectedMeal = meal }
                )
            }
        }

        // 2. Simple "Recipe Overlay" if a meal is clicked
        selectedMeal?.let { meal ->
            AlertDialog(
                onDismissRequest = { selectedMeal = null },
                title = { Text(meal.name, color = Color(0xFF31401C)) },
                text = { Text(meal.recipe) },
                confirmButton = {
                    TextButton(onClick = { selectedMeal = null }) {
                        Text("Close", color = Color(0xFF7C8C3F))
                    }
                },
                containerColor = Color(0xFFF7FDED) // Using your creamy background color
            )
        }
    }
}


//HAVE TO ADD THE USERS SAVED RECIPES

@Composable
fun MealItem(meal: Meal, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0097B2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Main Content Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Detail Row (Time, Servings, Calories)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${meal.time}  •  ${meal.servings}  •  ${meal.calories}",
                        color = Color.White.copy(alpha = 0.8f), // Slightly faded for hierarchy
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Delete Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}