package com.example.pantrypals.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//dark theme
private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    secondary = OrangeAccent,
    background = Color(0xFF1E1E1E),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// light theme
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = OrangeAccent,
    background = LightBackground,
    surface = LightBackground,

    onPrimary = Color.White,      // text/icons on green
    onSecondary = Color.White,    // text on orange
    onBackground = BrownText,     // main text
    onSurface = BrownText
)

@Composable
fun PantryPalsTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}