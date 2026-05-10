package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.model.Dessert

class DessertViewModel : ViewModel() {

    var uiState by mutableStateOf(DessertUiState())
        private set

    private var currentDessertPrice = 0
    private var currentDessertImageId = 0

    fun initialize(desserts: List<Dessert>) {
        if (currentDessertPrice == 0) {
            currentDessertPrice = desserts[0].price
            currentDessertImageId = desserts[0].imageId
        }
    }

    fun onDessertClicked(desserts: List<Dessert>) {

        uiState = uiState.copy(
            revenue = uiState.revenue + currentDessertPrice,
            dessertsSold = uiState.dessertsSold + 1
        )

        val dessertToShow = determineDessertToShow(
            desserts,
            uiState.dessertsSold
        )

        uiState = uiState.copy(
            currentDessertIndex = desserts.indexOf(dessertToShow)
        )

        currentDessertPrice = dessertToShow.price
        currentDessertImageId = dessertToShow.imageId
    }

    fun getCurrentImageId(): Int = currentDessertImageId
}