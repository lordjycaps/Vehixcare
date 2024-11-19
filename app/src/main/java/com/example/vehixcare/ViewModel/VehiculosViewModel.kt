package com.example.vehixcare.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehixcare.Model.Vehiculos
import com.example.vehixcare.Repository.VehixcareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VehiculosViewModel(private val repository: VehixcareRepository) : ViewModel() {

    private val _vehiculos = MutableStateFlow<List<Vehiculos>>(emptyList())
    val vehiculos: StateFlow<List<Vehiculos>> = _vehiculos.asStateFlow()

    init {
        loadVehiculos()
    }

    // Cargar vehículos para el usuario actual
    private fun loadVehiculos() {
        viewModelScope.launch {
            repository.getVehiculos().collect { vehiculoList ->
                _vehiculos.value = vehiculoList
            }
        }
    }

    // Método para insertar un nuevo vehículo
    fun insertVehiculo(vehiculo: Vehiculos) {
        viewModelScope.launch {
            repository.insertVehiculo(vehiculo)
            loadVehiculos()
        }
    }

    fun updateVehiculo(vehiculo: Vehiculos) {
        viewModelScope.launch {
            repository.updateVehiculo(vehiculo)
            loadVehiculos()
        }
    }

    fun deleteVehiculo(vehiculo: Vehiculos) {
        viewModelScope.launch {
            repository.deleteVehiculo(vehiculo)
            loadVehiculos()
        }
    }

    fun loadVehiculosForUser(userId: Int) {
        viewModelScope.launch {
            repository.getVehiculosForUser(userId).collect { vehiculoList ->
                _vehiculos.value = vehiculoList
            }
        }
    }
}

