package com.grigorevmp.dinorun.models

data class EarthState(
    val blocksList: ArrayList<EarthModel> = arrayListOf(),
    val maxBlocks: Int = 2,
    val speed: Int = 5
) {
    init {
        initBlocks()
    }

    fun initBlocks()
    {
        var startX = -EARTH_OFFSET.toFloat()
        for (i in 0 until maxBlocks) {
            val earth = EarthModel(
                xPos = startX,
                yPos = EARTH_Y_POSITION + (20 + i*10),
                size = deviceWidthInPixels + (EARTH_OFFSET*2).toFloat()
            )

            blocksList.add(earth)
            startX += earth.size
        }
    }

    fun moveForward()
    {
        val endPos = blocksList[maxBlocks-1].xPos + blocksList[maxBlocks-1].size
        for (i in 0 until maxBlocks)
        {
            val block = blocksList[i]
            block.xPos -= speed
            if ((block.xPos + block.size) < -EARTH_OFFSET ) {
                block.xPos = endPos
            }
        }
    }
}

data class EarthModel(
    var xPos: Float = 0f,
    var yPos: Float = 0f,
    var size: Float = deviceWidthInPixels + EARTH_OFFSET.toFloat()
)