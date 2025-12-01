package com.grigorevmp.dinorun.models

import androidx.compose.ui.graphics.Path
import kotlin.random.Random

data class CloudState(
    val cloudsList: ArrayList<CloudModel> = arrayListOf(),
    val maxClouds: Int = 10,
    val speed: Int = 1,

    val deviceWidthInPixels: Int
) {
    init {
        initCloud()
    }

    private fun initCloud() {
        var startX = 150

        (0 until maxClouds).forEach { i ->
            val cloud = CloudModel(
                xPos = startX,
                yPos = rand(0, 100)
            )
            cloudsList.add(cloud)

            startX += rand(150, deviceWidthInPixels)
        }
    }

    fun moveForward() {
        for (i in 0 until maxClouds) {
            val cloud = cloudsList[i]
            cloud.xPos -= speed
            if (cloud.xPos < -100) {
                cloud.xPos = rand(
                    deviceWidthInPixels,
                    deviceWidthInPixels * rand(1, 2)
                )
                cloud.yPos = rand(0, 100)
            }
        }
    }
}

data class CloudModel(
    var xPos: Int = 0,
    var yPos: Int = 0,
    var path: Path = CloudPath()
)

private val globalRandom: Random = Random.Default

fun rand(start: Int, end: Int): Int {
    require(start <= end) { "Illegal Argument: start ($start) must be <= end ($end)" }
    return globalRandom.nextInt(from = start, until = end + 1)
}

fun rand(start: Float, end: Float): Float {
    require(start <= end) { "Illegal Argument: start ($start) must be <= end ($end)" }
    val delta = end - start
    return globalRandom.nextFloat() * delta + start
}