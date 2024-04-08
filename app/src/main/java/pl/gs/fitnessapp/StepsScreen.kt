package pl.gs.fitnessapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun MainView(modifier: Modifier = Modifier, viewModel: MainViewModel, stepsViewModel: StepsViewModel) {

    val username = viewModel.username

    LaunchedEffect(username) {
        viewModel.viewModelScope.launch {
            stepsViewModel.getStepsOrInsert(username)
        }
    }

    val steps by stepsViewModel.steps.collectAsState()

    Column {
        Text(
            text = "Hello " + viewModel.username,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Text(text = "Steps: $steps", modifier = Modifier.padding(top = 16.dp))
        Button(
            onClick = { stepsViewModel.incrementStepCount(username) },
            colors = ButtonDefaults.buttonColors(Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = stringResource(R.string.increment_steps))
        }

        Button(
            onClick = { stepsViewModel.deleteSteps(username) },
            colors = ButtonDefaults.buttonColors(Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = stringResource(R.string.delete_steps))
        }

        Button(
            onClick = { viewModel.showLoginView() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = stringResource(R.string.button_back_to_login))
        }
    }
}