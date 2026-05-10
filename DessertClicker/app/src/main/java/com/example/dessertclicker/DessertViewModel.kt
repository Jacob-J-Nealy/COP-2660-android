package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class DessertViewModel : ViewModel() {

    var uiState by mutableStateOf(DessertUiState())
        private set

    fun onDessertClicked(price: Int) {
    }
}