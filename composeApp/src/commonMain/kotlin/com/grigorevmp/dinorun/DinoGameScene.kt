package com.grigorevmp.dinorun

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grigorevmp.dinorun.models.CLOUDS_SPEED
import com.grigorevmp.dinorun.models.CactusState
import com.grigorevmp.dinorun.models.CloudState
import com.grigorevmp.dinorun.models.DOUBT_FACTOR
import com.grigorevmp.dinorun.models.DinoState
import com.grigorevmp.dinorun.models.DinoTheme.colors
import com.grigorevmp.dinorun.models.EARTH_GROUND_STROKE_WIDTH
import com.grigorevmp.dinorun.models.EARTH_SPEED
import com.grigorevmp.dinorun.models.EARTH_Y_POSITION
import com.grigorevmp.dinorun.models.EarthState
import com.grigorevmp.dinorun.models.GameState
import com.grigorevmp.dinorun.models.MAX_CLOUDS
import com.grigorevmp.dinorun.models.currentScoreColor
import com.grigorevmp.dinorun.models.deviceWidthInPixels
import com.grigorevmp.dinorun.models.gameOverColor
import com.grigorevmp.dinorun.models.highScoreColor
import kotlinx.coroutines.delay

val showBounds = mutableStateOf(false)

@Composable
fun DinoGameScene(
    gameState: GameState
) {
    val cloudsState by remember {
        mutableStateOf(
            CloudState(
                maxClouds = MAX_CLOUDS,
                speed = CLOUDS_SPEED,
                deviceWidthInPixels = deviceWidthInPixels
            )
        )
    }
    val earthState by remember {
        mutableStateOf(
            EarthState(
                maxBlocks = 2,
                speed = EARTH_SPEED
            )
        )
    }
    val cactusState by remember { mutableStateOf(CactusState(cactusSpeed = EARTH_SPEED)) }
    val dinoState by remember { mutableStateOf(DinoState()) }

    val currentScore = gameState.currentScore
    val highScore = gameState.highScore
    val isGameOver = gameState.isGameOver

    // Настоящий игровой цикл
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (true) {
                // ~60 FPS
                delay(16L)

                gameState.increaseScore()
                cloudsState.moveForward()
                earthState.moveForward()
                cactusState.moveForward()
                dinoState.move()

                var collision = false
                for (cactus in cactusState.cactusList) {
                    val dinoBounds = dinoState.getBounds().deflate(DOUBT_FACTOR)
                    val cactusBounds = cactus.getBounds().deflate(DOUBT_FACTOR)
                    if (dinoBounds.overlaps(cactusBounds)) {
                        collision = true
                        break
                    }
                }
                if (collision) {
                    gameState.setGameOver()
                    break
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!gameState.isGameOver) {
                    dinoState.jump()
                } else {
                    cactusState.initCactus()
                    dinoState.init()
                    gameState.replay()
                }
            }
    ) {
        ShowBoundsSwitchView()
        HighScoreTextViews(currentScore = currentScore, highScore = highScore)
        Canvas(modifier = Modifier.weight(1f)) {
            val earthColor = Color(0xFF535353)
            val cloudsColor = Color(0xFFDBDBDB)
            val dinoColor = Color(0xFF535353)
            val cactusColor = Color(0xFF535353)

            EarthView(earthState = earthState, deviceWidthInPixels = deviceWidthInPixels, color = earthColor)
            CloudsView(cloudState = cloudsState, color = cloudsColor)
            DinoView(dinoState = dinoState, color = dinoColor)
            CactusView(cactusState = cactusState, color = cactusColor)
        }
    }

    GameOverTextView(
        isGameOver = isGameOver,
        modifier = Modifier
            .padding(top = 150.dp)
            .fillMaxWidth()
    )
}

fun DrawScope.DinoView(dinoState: DinoState, color: Color) {
    withTransform({
        translate(
            left = dinoState.xPos,
            top = dinoState.yPos - dinoState.path.getBounds().height
        )
    }) {
        // Можно логировать на всех платформах простым println
        println("Dino keyframe: ${dinoState.keyframe}")

        drawPath(
            path = dinoState.path,
            color = color,
            style = Fill
        )
        drawBoundingBox(color = Color.Green, rect = dinoState.path.getBounds())
    }
}

fun DrawScope.CloudsView(cloudState: CloudState, color: Color) {
    cloudState.cloudsList.forEach { cloud ->
        withTransform({
            translate(
                left = cloud.xPos.toFloat(),
                top = cloud.yPos.toFloat()
            )
        }) {
            drawPath(
                path = cloudState.cloudsList.first().path,
                color = color,
                style = Stroke(2f)
            )

            drawBoundingBox(color = Color.Blue, rect = cloud.path.getBounds())
        }
    }
}

fun DrawScope.EarthView(
    earthState: EarthState,
    deviceWidthInPixels: Int,
    color: Color
) {
    // Ground Line
    drawLine(
        color = color,
        start = Offset(x = 0f, y = EARTH_Y_POSITION),
        end = Offset(x = deviceWidthInPixels.toFloat(), y = EARTH_Y_POSITION),
        strokeWidth = EARTH_GROUND_STROKE_WIDTH
    )

    earthState.blocksList.forEach { block ->
        drawLine(
            color = color,
            start = Offset(x = block.xPos, y = EARTH_Y_POSITION + 20),
            end = Offset(x = block.size, y = EARTH_Y_POSITION + 20),
            strokeWidth = EARTH_GROUND_STROKE_WIDTH / 5,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 40f), 0f)
        )
        drawLine(
            color = color,
            start = Offset(x = block.xPos, y = EARTH_Y_POSITION + 30),
            end = Offset(x = block.size, y = EARTH_Y_POSITION + 30),
            strokeWidth = EARTH_GROUND_STROKE_WIDTH / 5,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 50f), 40f)
        )
    }
}

fun DrawScope.CactusView(cactusState: CactusState, color: Color) {
    cactusState.cactusList.forEach { cactus ->
        withTransform({
            scale(cactus.scale, cactus.scale)
            translate(
                left = cactus.xPos.toFloat(),
                top = cactus.getBounds().top * cactus.scale
            )
            rotate(cactus.angle)
        }) {
            drawPath(
                path = cactus.path,
                color = color,
                style = Fill
            )
            drawBoundingBox(color = Color.Red, rect = cactus.path.getBounds())
        }
    }
}

@Composable
fun HighScoreTextViews(currentScore: Int, highScore: Int) {
    Spacer(modifier = Modifier.padding(top = 50.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(text = "Score", style = TextStyle(color = colors.highScoreColor))
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Text(
            text = "$highScore".padStart(5, '0'),
            style = TextStyle(color = colors.highScoreColor)
        )
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Text(
            text = "$currentScore".padStart(5, '0'),
            style = TextStyle(color = colors.currentScoreColor)
        )
    }
}

@Composable
fun ShowBoundsSwitchView() {
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Show Bounds",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Switch(
            checked = showBounds.value,
            onCheckedChange = { showBounds.value = it }
        )
    }
}

/**
 * KMP-совместимый вариант без R.drawable / painterResource.
 * Иконку можно передать извне через слот [replayIcon].
 */
@Composable
fun GameOverTextView(
    isGameOver: Boolean = true,
    modifier: Modifier = Modifier,
    replayIcon: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(
            text = if (isGameOver) "GAME OVER" else "",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            letterSpacing = 5.sp,
            style = TextStyle(
                color = colors.gameOverColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        if (isGameOver && replayIcon != null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                replayIcon()
            }
        }
    }
}

fun DrawScope.drawBoundingBox(color: Color, rect: Rect, name: String? = null) {
    name?.let {
        println("drawBounds $name $rect")
    }
    if (showBounds.value) {
        drawRect(color, rect.topLeft, rect.size, style = Stroke(3f))
        val deflated = rect.deflate(DOUBT_FACTOR)
        drawRect(
            color,
            deflated.topLeft,
            deflated.size,
            style = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 4f), 0f)
            )
        )
    }
}

fun Rect.collided(other: Rect, doubtFactor: Float = 0f): Boolean {
    if (right >= (other.left + doubtFactor) && right <= (other.right - doubtFactor)) {
        return true
    }
    // Остальной код был закомментирован в оригинале — оставляем поведение как есть
    return false
}