package pl.gs.fitnessapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
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
    private lateinit var stepsViewModel: StepsViewModel
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        val stepsViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(StepsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return StepsViewModel(database.dao, sensorManager, stepSensor, viewModel) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        stepsViewModel = ViewModelProvider(this, stepsViewModelFactory)[StepsViewModel::class.java]

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

    override fun onResume() {
        super.onResume()
        stepsViewModel.registerSensorListener()
    }

    override fun onPause() {
        super.onPause()
        stepsViewModel.unregisterSensorListener()
    }

}