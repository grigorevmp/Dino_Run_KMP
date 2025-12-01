package com.grigorevmp.dinorun.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class GameState(
    initialScore: Int = 0,
    initialHighScore: Int = 0
) {

    var currentScore by mutableStateOf(initialScore)
        private set

    var highScore by mutableStateOf(initialHighScore)
        private set

    var isGameOver by mutableStateOf(false)
        private set

    fun increaseScore(step: Int = 1) {
        currentScore += step
    }

    fun setGameOver() {
        isGameOver = true
    }

    fun replay() {
        if (currentScore > highScore) {
            highScore = currentScore
        }
        currentScore = 0
        isGameOver = false
    }
}