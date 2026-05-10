package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.model.Dessert

class DessertViewModel : ViewModel() {

    var uiState by mutableStateOf(DessertUiState())
        private set

    private var desserts: List<Dessert> = emptyList()

    fun setDesserts(list: List<Dessert>) {
        desserts = list
    }

    fun getCurrentDessert(): Dessert {
        return desserts.getOrElse(uiState.currentDessertIndex) {
            desserts.first()
        }
    }

    fun onDessertClicked() {
        if (desserts.isEmpty()) return

        val currentDessert = getCurrentDessert()

        val newSold = uiState.dessertsSold + 1
        val newRevenue = uiState.revenue + currentDessert.price
        val newIndex = determineIndex(newSold)

        uiState = uiState.copy(
            dessertsSold = newSold,
            revenue = newRevenue,
            currentDessertIndex = newIndex
        )
    }

    private fun determineIndex(sold: Int): Int {
        var index = 0

        for (i in desserts.indices) {
            if (sold >= desserts[i].startProductionAmount) {
                index = i
            } else break
        }
        return index
    }
}