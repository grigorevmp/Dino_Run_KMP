package com.grigorevmp.dinorun

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.grigorevmp.dinorun.models.GameState

@Composable
fun App() {
    val gameState = remember { GameState() }

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        DinoGameScene(
            gameState = gameState
        )
    }
}
