package pl.gs.fitnessapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gs.fitnessapp.ui.theme.Gold

@Composable
fun MainView(modifier: Modifier = Modifier, viewModel: MainViewModel, stepsViewModel: StepsViewModel) {

    val username = viewModel.username

    LaunchedEffect(username) {
        viewModel.viewModelScope.launch {
            stepsViewModel.getStepsOrInsert(username)
        }
    }

    val steps by stepsViewModel.steps.collectAsState()

    val maxSteps = 100
    val progress = (steps.toFloat() / maxSteps.toFloat()) * 100
    val text = stringResource(R.string.congratulations)

    val color = when {
        progress < 25 -> Color.Red
        progress < 50 -> Color(0xFFFFA500)
        progress < 75 -> Color.Yellow
        else -> Color.Green
    }

    Column(
        modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = stringResource(R.string.app_greeting) + viewModel.username.replaceFirstChar { it.uppercase() },
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        PieChart(
            steps = steps,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(200.dp)
                .align(Alignment.CenterHorizontally),
            progressColor = color,
            maxSteps = maxSteps,
            congratsText = text
        )

        Text(
            text = "Steps: $steps",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = { stepsViewModel.deleteSteps(username) },
            colors = ButtonDefaults.buttonColors(Color(0xFFFFA500)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = stringResource(R.string.delete_steps))
        }

        Button(
            onClick = { stepsViewModel.deleteAllData() },
            colors = ButtonDefaults.buttonColors(Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = stringResource(R.string.delete_all_data))
        }

        Button(
            onClick = { viewModel.showLoginView() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.button_back_to_login)
            )
        }
    }
}

@Composable
fun PieChart(
    steps: Int,
    modifier: Modifier = Modifier,
    progressColor: Color,
    maxSteps: Int,
    congratsText: String
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.minDimension / 2

                val progress = (steps.toFloat() / maxSteps.toFloat()) * 360

                val startAngle = 0f

                drawArc(
                    color = progressColor,
                    startAngle = startAngle,
                    sweepAngle = progress,
                    useCenter = true,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(32f, cap = StrokeCap.Round)
                )

                if (steps >= maxSteps) {
                    drawIntoCanvas {
                        val paint = android.graphics.Paint().apply {
                            color = Gold.hashCode()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 64f
                        }
                        it.nativeCanvas.drawText(congratsText, centerX, centerY, paint)
                    }
                }

            }
        )

    }
}


