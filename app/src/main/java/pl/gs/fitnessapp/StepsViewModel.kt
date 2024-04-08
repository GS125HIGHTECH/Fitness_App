package pl.gs.fitnessapp

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
    private val dao: StepsDao
) : ViewModel() {

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

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
    fun incrementStepCount(username: String) {
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

}