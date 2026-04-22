package com.example.pantrypals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.example.pantrypals.ui.theme.PantryPalsTheme
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.SelectInstance
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
//import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState



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
                                    label = { Text("Profile",color = Color(0xFF31401C)) },
                                    selected = currentRoute == Screen.Profile.route,
                                    onClick = {
                                        navController.navigate(Screen.Profile.route)
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
                                composable(Screen.Home.route) {
                                    HomeScreen() // This now points to the function in HomeScreen.kt
                                }
                                composable(Screen.Profile.route) {
                                    ProfileScreen() // Points to ProfileScreen.kt
                                }
                                composable(Screen.Preferences.route) {
                                    PreferencesScreen() // Points to PreferencesScreen.kt
                                }
                                composable(Screen.SavedMeals.route) {
                                    SavedMealsScreen() // Points to SavedMealsScreen.kt
                                }
                            }
                        }
                    }
                }
            }
        }
    }

// NAVBAR ITEMS: Home, profile, preferences, saved meals

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
//fun ProfileScreen() {
//    Text("User Profile Information")
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