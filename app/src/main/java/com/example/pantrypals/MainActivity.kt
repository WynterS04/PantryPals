package com.example.pantrypals


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import com.example.pantrypals.ui.theme.PantryPalsTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pantrypals.screens.HomeScreen
import com.example.pantrypals.screens.MealDetailScreen
import com.example.pantrypals.screens.PreferencesScreen
import com.example.pantrypals.screens.SavedMealsScreen
import com.example.pantrypals.viewmodel.MealsViewModel
import com.example.pantrypals.viewmodel.PreferencesViewModel


//fix highlights

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantryPalsTheme() {
                    val navController = rememberNavController()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    //user prev preferences
                    val prefsVM: PreferencesViewModel = viewModel()

                    //prompt
                    var responseText by remember { mutableStateOf("") }
                    var isLoading by remember { mutableStateOf(false) }

                    //meal viewmodel
                    val mealsVM: MealsViewModel = viewModel()
                    var isSaved by remember {mutableStateOf(false)}


                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet (
                                drawerContainerColor = Color(0xFFF7FDED)
                            ){
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically // Aligns image and text perfectly
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.pantrypals),
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape) // Clips the image to a circle
                                            .border(
                                                width = 1.dp, // Adjust the border thickness
                                                color = Color(0xFF31401C), // 2. Apply your deep forest green
                                                shape = CircleShape // 3. Ensure the border is circular
                                            ),
                                    contentScale = ContentScale.Crop
                                    )

                                    Spacer(modifier = Modifier.width(12.dp)) // Adds a small gap between image and text

                                    Text(
                                        text = "Menu",
                                        color = Color(0xFF31401C),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                                HorizontalDivider()

                                NavigationDrawerItem(
                                    label = { Text("Home",color = Color(0xFF31401C)) },
                                    selected = currentRoute == Screen.Home.route,
                                    onClick = {
                                        navController.navigate(Screen.Home.route)
                                        scope.launch { drawerState.close() }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedTextColor = Color(0xFF31401C),
                                        selectedTextColor = Color(0xFFF7FDED) // Maybe white when highlighted?
                                    )
                                )
                                NavigationDrawerItem(
                                    label = { Text("Preferences",color = Color(0xFF31401C)) },
                                    selected = currentRoute == Screen.Preferences.route,
                                    onClick = {
                                        navController.navigate(Screen.Preferences.route)
                                        scope.launch { drawerState.close() }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedTextColor = Color(0xFF31401C),
                                        selectedTextColor = Color(0xFFF7FDED) // Maybe white when highlighted?
                                    )
                                )
                                NavigationDrawerItem(
                                    label = { Text("Saved Meals",color = Color(0xFF31401C)) },
                                    selected = currentRoute == Screen.SavedMeals.route,
                                    onClick = {
                                        navController.navigate(Screen.SavedMeals.route)
                                        scope.launch { drawerState.close() }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        unselectedTextColor = Color(0xFF31401C),
                                        selectedTextColor = Color(0xFFF7FDED) // Maybe white when highlighted?
                                    )
                                )
                            }
                        }
                    ) {
                        // This is the main screen content
                        Scaffold(
                            topBar = {
                                CenterAlignedTopAppBar(
                                    title = { Text("PantryPals", color = Color(0xFFF7FDED)) },
                                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                        containerColor = Color(0xFF7C8C3F)
                                    ),
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch { drawerState.open() }
                                        }) {
                                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFFF7FDED))
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            // The actual NavHost that switches your fragments/screens
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Home.route,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable(Screen.Preferences.route) {

                                    val coroutineScope = rememberCoroutineScope()



                                    val generativeModel = GenerativeModel(
                                        modelName = "gemini-3.1-flash-lite-preview",
                                        apiKey = "insert_here"
                                    )


                                    PreferencesScreen(
                                        viewModel = prefsVM,
                                        onGenerate = { prefs ->

                                            coroutineScope.launch {
                                                isLoading = true

                                                try {
                                                    val prompt = """
                                                    You are a recipe generator. You MUST follow this exact format and do not deviate.
                                                    
                                                    Return ONLY in this structure:
                                                    
                                                    Title: <recipe name>
                                                    
                                                    Details:
                                                    Prep Time: 
                                                    Cook Time: 
                                                    Servings: 
                                                    
                                                    Ingredients:
                                   
                                                    
                                                    Instructions:
                                                    
                                                    
                                                    Nutritional Profile:
                                                    Protein: 
                                                    Calories: 
                                                    Carbs: 
                                                    
                                                    Rules:
                                                    - Give full bulleted list of Ingredients, each ingredient on a separate line
                                                    - Give full numbered list of Instructions, each insturction on it's own line 
                                                    - Do NOT explain anything
                                                    - Do NOT use markdown or symbols like ** or ###
                                                    - Keep each section exactly as labeled above
                                                    - Ensure instructions are numbered
                                                    
                                                    Create a recipe using:
                                                    Proteins: ${prefs.selectedProteins.joinToString()}
                                                    Avoid: ${prefs.dislikes}
                                                    Include: ${prefs.favoriteIngredients}
                                                    Dietary restrictions: ${prefs.dietaryRestrictions}
                                                    Calories goal: ${prefs.calories}
                                                    Protein goal: ${prefs.protein}
                                                    Carbs goal: ${prefs.carbs}
                                                    """.trimIndent()

                                                    val response =
                                                        generativeModel.generateContent(prompt)

                                                    responseText = response.text ?: "No response"

                                                    //navigate to recipe screen
                                                    navController.navigate("mealDetail") // or Recipe screen

                                                } catch (e: Exception) {
                                                    responseText = "Error: ${e.message}"
                                                }

                                                isLoading = false
                                            }
                                        }
                                    )
                                }
                                composable(Screen.Home.route) {
                                    HomeScreen(navController) // This now points to the function in HomeScreen.kt
                                }
                                composable(Screen.SavedMeals.route) {
                                    SavedMealsScreen() // Points to SavedMealsScreen.kt
                                }
                                composable("mealDetail") {
                                    MealDetailScreen(
                                        recipeText = responseText,
                                        isSaved = isSaved,
                                        onSave = {
                                            mealsVM.addMeal(responseText)
                                            isSaved = !isSaved
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

// NAVBAR ITEMS: Home, preferences, saved meals

//@Composable
//fun HomeScreen(modifier: Modifier) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Welcome to the Home Screen!", style = MaterialTheme.typography.headlineMedium)
//        // Add your main content here
//    }
//}
//
//@Composable
//fun SavedMealsScreen() {
//    Text("Your Favorite Recipes")
//}
//
//@Composable
//fun PreferencesScreen() {
//    Text("Your preferences")
//}
