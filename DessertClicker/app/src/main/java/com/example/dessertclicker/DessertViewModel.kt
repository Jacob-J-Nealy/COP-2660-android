package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DessertViewModel : ViewModel() {

    var uiState by mutableStateOf(DessertUiState())
        private set

    fun onDessertClicked(price: Int) {
        uiState = uiState.copy(
            revenue = uiState.revenue + price,
            dessertsSold = uiState.dessertsSold + 1
        )
    }

    fun updateDessert(index: Int) {
        uiState = uiState.copy(currentDessertIndex = index)
    }
}