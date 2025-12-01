package com.grigorevmp.dinorun.models

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

data class CactusState(
    val cactusList: ArrayList<CactusModel> = ArrayList(),
    val cactusSpeed: Int = EARTH_SPEED,
) {
    init {
        initCactus()
    }

    fun initCactus()
    {
        cactusList.clear()
        var startX = deviceWidthInPixels + 150
        val cactusCount = 3

        (0 until cactusCount).forEach { _ ->
            val cactus = CactusModel(
                scale = rand(0.75f, 1.2f),
                angle = 0f,
                xPos = startX,
                yPos = EARTH_Y_POSITION.toInt() + rand(20, 30)
            )
            cactusList.add(cactus)

            startX += distanceBetweenCactus
            startX += rand(100, distanceBetweenCactus)
        }
    }

    fun moveForward()
    {
        cactusList.forEach { cactus ->
            cactus.xPos -= cactusSpeed
        }

        if (cactusList.first().xPos < -250) {
            cactusList.removeAt(0)

            val cactus = CactusModel(
                scale = rand(0.75f, 1.2f),
                angle = 0f,
                xPos = nextCactusX(cactusList.last().xPos),
                yPos = EARTH_Y_POSITION.toInt() + rand(20, 30)
            )

            cactusList.add(cactus)
        }
    }

    fun nextCactusX(lastX: Int): Int
    {
        var nextX = lastX + distanceBetweenCactus
        nextX += rand(100, distanceBetweenCactus)
        if (nextX < deviceWidthInPixels)
            nextX += (deviceWidthInPixels - nextX)
        return nextX
    }
}

data class CactusModel(
    val scale: Float = 1f,
    val angle: Float = 0f,
    var xPos: Int = 0,
    var yPos: Int = 0,
    var path: Path = CactusPath()
) {

    fun getBounds() : Rect
    {
        return Rect(
            left = xPos.toFloat(),
            top = yPos.toFloat() - (path.getBounds().height * scale),
            right = xPos + (path.getBounds().width * scale),
            bottom = yPos.toFloat()
        )
    }
}