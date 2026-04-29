package com.example.pantrypals

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pantrypals.data.Meal
import com.example.pantrypals.viewmodel.MealsViewModel
import java.io.File
import java.io.FileOutputStream

//data class Meal(
//    val id: Int,
//    val name: String,
//    val recipe: String,
//    val time: String,      // e.g., "20 min"
//    val servings: String,  // e.g., "2 servings"
//    val calories: String   // e.g., "450 kcal"
//)
@Composable
fun SavedMealsScreen(viewModel: MealsViewModel) {
    val context = LocalContext.current
    val savedMeals = viewModel.meals
//    // WILL CHANGE THESE WHEN WE HAVE DYNAMIC DATA
//    val savedMeals = remember {
//        mutableStateListOf(
//            Meal(1, "Pesto Pasta", "Boil pasta...", "15 min", "2 servings", "500 kcal"),
//            Meal(2, "Chickpea Salad", "Mix veggies...", "10 min", "1 serving", "320 kcal")
//        )
//    }

    // State to track which recipe is currently being viewed
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(savedMeals) { meal ->
                MealItem(
                    meal = meal,
                    onDelete = { savedMeals.remove(meal) },
                    onExport = {
                        println("Exporting your meal as a PDF")
                        val formatedRecipe = formatMeal(meal);
                        exportToPdf(context, formatedRecipe)
                    },
                    onClick = { selectedMeal = meal }
                )
            }
        }

        // Recipe Overlay if a meal is clicked
        selectedMeal?.let { meal ->
            AlertDialog(
                onDismissRequest = { selectedMeal = null },
                // Use the first line as title
                title = { Text(meal.text.substringBefore("\n"), color = Color(0xFF31401C)) },
                // Use the rest as recipe text
                text = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        Text(
                            meal.text.substringAfter("\n")
                            )

                        }
                       },
                confirmButton = {
                    TextButton(onClick = { selectedMeal = null }) {
                        Text("Close", color = Color(0xFF7C8C3F))
                    }
                },
                containerColor = Color(0xFFF7FDED)
            )
        }
    }
}


//export logic
fun exportToPdf(context: Context, text: String) {
    //to find exported file on emulator: View, Tool Windows, Device Explorer, then follow the path given
    // (normally storage, emulated, 0, android, data, com.example.pantrypals, files, download)
    val pdfDocument = PdfDocument()
    // Create a page info (A4 size: 595 x 842 points)
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas: Canvas = page.canvas
    val paint = Paint()
    paint.textSize = 12f

    // Handle multi-line text
    val x = 10f
    var y = 25f
    for (line in text.split("\n")) {
        canvas.drawText(line, x, y, paint)
        y += paint.descent() - paint.ascent()
    }

    pdfDocument.finishPage(page)

    // Save the file to the Downloads folder
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "GeminiRecipe.pdf")

    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF Saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF", Toast.LENGTH_SHORT).show()
    }

    pdfDocument.close()
}


//meal format logic
fun formatMeal(meal: Meal): String {
    // Just returns the whole block of text
    return meal.text
}

//HAVE TO ADD THE USERS SAVED RECIPES

@Composable
fun MealItem(
    meal: Meal,
    onDelete: () -> Unit,
    onExport: () -> Unit,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0097B2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Title is the first line
                Text(
                    text = meal.text.substringBefore("\n"),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                // Subtitle/Details is the rest
                Text(
                    text = "View full recipe details...",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // The Three Dots Menu
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White
                    )
                }

                // The Popup Menu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = Color(0xFFF7FDED) // Using your light creamy background
                ) {
                    DropdownMenuItem(
                        text = { Text("Export PDF", color = Color(0xFF31401C)) },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF31401C)) },
                        onClick = {
                            expanded = false
                            onExport()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Saved Meal", color = Color.Red) },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}