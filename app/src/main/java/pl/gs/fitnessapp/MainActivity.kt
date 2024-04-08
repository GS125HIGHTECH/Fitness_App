package pl.gs.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import pl.gs.fitnessapp.data.StepsDatabase
import pl.gs.fitnessapp.ui.theme.FitnessAppTheme

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            StepsDatabase::class.java,
            "steps.db"
        ).build()
    }

    private val viewModel by viewModels<MainViewModel>()
    private val stepsViewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StepsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StepsViewModel(database.dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val stepsViewModel by viewModels<StepsViewModel>(factoryProducer = { stepsViewModelFactory })

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            FitnessAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (viewModel.showMainView) {
                        MainView(Modifier, viewModel, stepsViewModel)
                    } else {
                        Greeting(Modifier, viewModel)
                    }
                }
            }
        }
    }
}