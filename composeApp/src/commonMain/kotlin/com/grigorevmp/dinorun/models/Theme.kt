package com.grigorevmp.dinorun.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ---------- Core palette ----------

data class DinoColors(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val secondaryVariant: Color,
    val background: Color,
    val surface: Color,
    val error: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onError: Color,
    val isLight: Boolean
)

// ---------- Light / Dark instances ----------

val LightDinoColors = DinoColors(
    primary = Color(0xFF855446),
    primaryVariant = Color(0xFF9C684B),
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF0AC9F0),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    isLight = true
)

val DarkDinoColors = DinoColors(
    primary = Color(0xFF1F1F1F),
    primaryVariant = Color(0xFF3E2723),
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF03DAC5),
    background = Color(0xFF121212),
    surface = Color.Black,
    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black,
    isLight = false
)

// ---------- Access via CompositionLocal ----------

val LocalDinoColors = staticCompositionLocalOf { LightDinoColors }

object DinoTheme {
    val colors: DinoColors
        @Composable get() = LocalDinoColors.current
}

// ---------- Extensions ----------

val DinoColors.earthColor: Color
    get() = if (isLight) Color(0xFF535353) else Color(0xFFACACAC)

val DinoColors.cloudColor: Color
    get() = if (isLight) Color(0xFFDBDBDB) else Color(0xFFACACAC)

val DinoColors.dinoColor: Color
    get() = if (isLight) Color(0xFF535353) else Color(0xFFACACAC)

val DinoColors.cactusColor: Color
    get() = if (isLight) Color(0xFF535353) else Color(0xFFACACAC)

val DinoColors.gameOverColor: Color
    get() = if (isLight) Color(0xFF000000) else Color(0xFFFFFFFF)

val DinoColors.currentScoreColor: Color
    get() = if (isLight) Color(0xFF535353) else Color(0xFFACACAC)

val DinoColors.highScoreColor: Color
    get() = if (isLight) Color(0xFF757575) else Color(0xFF909191)