package com.example.export

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
//for pdf
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import java.io.File
import java.io.FileOutputStream
//for new button
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeminiSimpleScreen()
        }
    }
}


@Composable
fun GeminiSimpleScreen() {
    val context = LocalContext.current
    var responseText by remember { mutableStateOf("Ready to ask Gemini!") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val generativeModel = remember {
        GenerativeModel(
            modelName = "gemini-3.1-flash-lite-preview",
            apiKey = "api key here"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        responseText = "Thinking..."
                        try {
                            val prompt = "Give me a recipe. Title, Time, calories, servings, ingredients, and steps."
                            val response = generativeModel.generateContent(prompt)
                            responseText = response.text ?: "No response generated."
                        } catch (e: Exception) {
                            responseText = "Error: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Asking..." else "Get Recipe")
            }

            // The Export Button
            Button(
                onClick = { exportToPdf(context, responseText) },
                // Only enable if we have a real recipe (not the starting text)
                enabled = responseText != "Ready to ask Gemini!" && !isLoading
            ) {
                Text("Export PDF")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. The Response Display (Your Box/Text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // This lets the text area take up the remaining space
        ) {
            Text(
                text = responseText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


fun exportToPdf(context: Context, text: String) {
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
    val file =
        File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "GeminiRecipe.pdf")

    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF Saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF", Toast.LENGTH_SHORT).show()
    }

    pdfDocument.close()
}