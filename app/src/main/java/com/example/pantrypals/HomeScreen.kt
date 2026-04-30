package com.example.pantrypals

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pantrypals.R

// for profile image
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
fun HomeScreen(navController: NavController) {
    val primaryBlue = Color(0xFF009BB5)
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var name by rememberSaveable {
        mutableStateOf(prefs.getString("user_name", "") ?: "")
    }
    var showDialog by remember {
        mutableStateOf(name.isBlank())
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { }, // prevents tapping outside to close
            title = { Text("Welcome!") },
            text = {
                Column {
                    Text("Enter your name")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            prefs.edit().putString("user_name", name).apply()

                            showDialog = false
                        }
                    }
                ) {
                    Text("Continue")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // profile image, currently has blank image placeholder vector
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.LightGray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pantrypals),
                    contentDescription = "PantryPals Logo",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF7C8C3F), // matches your top bar green
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (name.isNotBlank()) "Hi, $name" else "Jane Doe",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(50.dp)) // lowering the buttons positions

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            ProfileButtonItems(
                text = "Preferences",
                color = primaryBlue,
                onClick = {
                    navController.navigate(Screen.Preferences.route) // recommend keeping this formatting in reference to MainActivity
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            ProfileButtonItems(
                text = "Saved Meals",
                color = primaryBlue,
                onClick = {
                    navController.navigate(Screen.SavedMeals.route)
                }
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.Home.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD16B2F)
                )
            ) {
                Text("Generate New Recipe")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProfileButtonItems(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(250.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )


            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }


}