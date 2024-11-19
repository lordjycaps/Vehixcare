package com.example.vehixcare.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.ViewModel.VehiculosViewModel

class VehiculosViewModelFactory(
    private val repository: VehixcareRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehiculosViewModel::class.java)) {
            return VehiculosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
