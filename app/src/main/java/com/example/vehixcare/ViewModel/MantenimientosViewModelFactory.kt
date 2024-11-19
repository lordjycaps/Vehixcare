package com.example.vehixcare.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vehixcare.Repository.VehixcareRepository

class MantenimientosViewModelFactory(
    private val repository: VehixcareRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MantenimientosViewModel::class.java)) {
            return MantenimientosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
