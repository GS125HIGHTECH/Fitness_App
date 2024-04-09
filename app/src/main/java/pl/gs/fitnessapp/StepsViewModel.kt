package pl.gs.fitnessapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.gs.fitnessapp.data.Steps
import pl.gs.fitnessapp.data.StepsDao

class StepsViewModel(
    private val dao: StepsDao,
    private val sensorManager: SensorManager,
    private val stepSensor: Sensor?,
    private val mainViewModel: MainViewModel
) : ViewModel(), SensorEventListener {

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    fun registerSensorListener() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun unregisterSensorListener() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == stepSensor) {
            incrementStepCount(mainViewModel.username)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun getStepsOrInsert(username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val steps = dao.getSteps(username)
                if (steps == null) {
                    dao.insertSteps(Steps(username, 0))
                    _steps.value = 0
                } else {
                    _steps.value = steps.stepsCount
                }
            }
        }
    }
    private fun incrementStepCount(username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentSteps = dao.getSteps(username)
                if (currentSteps != null) {
                    val newSteps = currentSteps.stepsCount + 1
                    dao.updateSteps(Steps(username, newSteps))
                    _steps.value = newSteps
                }
            }
        }
    }

    fun deleteSteps(username: String) {
        viewModelScope.launch {
            dao.deleteSteps(username)
            _steps.value = 0
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            dao.deleteAllData()
            mainViewModel.showLoginView()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stepSensor?.let {
            sensorManager.unregisterListener(this)
        }
    }

}