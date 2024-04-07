package pl.gs.fitnessapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var username by mutableStateOf("")
    var showMainView by mutableStateOf(false)
        private set

    fun updateUsername(input: String) {
        username = input
    }

    fun showMainView() {
        showMainView = true
    }

    fun showLoginView() {
        showMainView = false
    }
}