package pl.gs.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.gs.fitnessapp.ui.theme.FitnessAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            FitnessAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (viewModel.showMainView) {
                        MainView(Modifier, viewModel)
                    } else {
                        Greeting(Modifier, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, viewModel: MainViewModel) {


    Column {
        Text(
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        EditNumberField(
            label = R.string.name_label,
            leadingIcon = R.drawable.baseline_login_24,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done,
            ),
            value = viewModel.username,
            onValueChanged = { username -> viewModel.updateUsername(username) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        )

        Button(
            onClick = { viewModel.showMainView() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = "Go to Main View")
        }

    }
}

@Composable
fun MainView(modifier: Modifier = Modifier, viewModel: MainViewModel) {

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
        Button(
            onClick = { viewModel.showLoginView() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = "Back")
        }
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        modifier = modifier.focusRequester(focusRequester),
        onValueChange = { newValue -> onValueChanged(newValue)
        },
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}