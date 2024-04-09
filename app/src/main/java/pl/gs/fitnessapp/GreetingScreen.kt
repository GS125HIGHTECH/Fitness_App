package pl.gs.fitnessapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Greeting(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            val usernameState = remember { mutableStateOf(viewModel.username) }

            EditNumberField(
                label = R.string.name_label,
                leadingIcon = R.drawable.baseline_login_24,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                ),
                value = usernameState.value,
                onValueChanged = { username ->
                    if (username.length <= 30) {
                        usernameState.value = username
                    }
                },
                onImeActionPerformed = { action ->
                    if (action == ImeAction.Done) {
                        if (!isError(usernameState.value)) {
                            viewModel.updateUsername(usernameState.value)
                            viewModel.showMainView()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            )

            val isError = isError(usernameState.value)

            if (usernameState.value.isNotEmpty()) {
                if (isError) {
                    Text(
                        text = stringResource(R.string.validate_input_name),
                        fontSize = 12.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp)
                    )
                }
            }

            Button(
                onClick = {
                    if (!isError) {
                        viewModel.updateUsername(usernameState.value)
                        viewModel.showMainView()
                    }
                },
                enabled = !isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 32.dp, end = 32.dp)
            ) {
                Text(text = stringResource(R.string.button_login))
            }

        }
    }
}

private fun isError(username: String): Boolean {
    return username.isEmpty() || username.length < 4 || username.length > 20 || !username.matches(Regex("^[a-zA-Z0-9]*$"))
}