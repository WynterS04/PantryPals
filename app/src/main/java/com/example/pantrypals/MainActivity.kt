package com.example.pantrypals


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.SelectInstance
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
//icon imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.lifecycle.viewmodel.compose.viewModel
//navigation imports
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pantrypals.data.Meal
import com.example.pantrypals.viewmodel.MealsViewModel
import androidx.compose.foundation.lazy.items


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                AppNavigation()
        }
    }
}


@Composable
fun AppNavigation() {
    //prompt
    var responseText by remember { mutableStateOf("Ready to ask Gemini") }
    var isLoading by remember { mutableStateOf(false) }
    var meal: String? = ""


    //saved meals
    var saveMessage by remember { mutableStateOf("") }
    var isSaved by remember {mutableStateOf(false)}

    val viewModel: MealsViewModel = viewModel()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val generativeModel = remember{
        GenerativeModel(
            //model we want to use
            modelName = "gemini-3.1-flash-lite-preview",
            //api key
            apiKey = "Insert here"
        )
    }
    NavHost(navController = navController, startDestination = "prompt") {
        composable("prompt") {
            PromptScreen(
                onGenerate = {
                    coroutineScope.launch {
                        isLoading = true
                        responseText = "Thinking..."


                        try{
                            val prompt = "Make me a recipe with chicken, that does not include nuts but includes lettuce"
                            val response = generativeModel.generateContent(prompt)
                            meal = response.text


                            responseText = response.text ?: "No response generated."


                        } catch (e: Exception){
                            responseText = "Error: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                    navController.navigate("recipe")
                }
            )
        }


        composable("recipe") {
            RecipeScreen(
                responseText = responseText,
                isSaved = isSaved,
                onSave = {
                    viewModel.addMeal(meal)
                    navController.navigate("confirm")
                }
            )
        }


        composable("confirm") {
            ConfirmMealSaved(
                saveMessage = "Meal Saved!",
                onClick = {
                    navController.navigate("saved_meals")
                }
            )
        }


        composable("saved_meals") {
            SavedMealsScreen(meals = viewModel.meals)
        }
    }
}


@Composable
fun PromptScreen(onGenerate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onGenerate) {
                Text("Generate Recipe")
            }
        }
    }
}


@Composable
fun RecipeScreen(responseText : String, isSaved: Boolean, onSave: () -> Unit) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(30.dp)
            ) {
                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) Color.Red else Color.Gray
                    )
                }
                Text(text = "Save this recipe")
            }
        }
        item {
            Text(
                text = responseText,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Composable
fun ConfirmMealSaved(saveMessage: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            if (saveMessage.isNotEmpty()) {
                Text(text = saveMessage)
            }
        }
        Box(
            modifier = Modifier
                .weight(.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Button(onClick = onClick) {
                Text("Saved Meals")
            }
        }
    }
}


@Composable
fun SavedMealsScreen(meals: List<Meal>) {
    LazyColumn (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        items(meals) { meal ->
            Text(meal.text)
        }
    }
}

@Composable
fun MealList(
    mealList: List<Meal>,
    onFavoriteClick: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = mealList, key = { it.id }) { meal ->
            MealItem(
                meal = meal,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun MealItem(
    meal: Meal,
    onFavoriteClick: (Meal) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(30.dp)
            ) {
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            IconButton(onClick = { onFavoriteClick(meal) }) {
                Icon(
                    imageVector = if (meal.isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = "Favorite",
                    tint = if (meal.isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}
