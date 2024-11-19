package com.example.vehixcare.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehixcare.Model.Mantenimientos
import com.example.vehixcare.Model.Vehiculos
import com.example.vehixcare.Repository.VehixcareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MantenimientosViewModel(private val repository: VehixcareRepository) : ViewModel() {

    private val _mantenimientos = MutableStateFlow<List<Mantenimientos>>(emptyList())
    val mantenimientos: StateFlow<List<Mantenimientos>> = _mantenimientos.asStateFlow()

    // Cargar mantenimientos de un vehículo específico
    fun loadMantenimientosForVehiculo(vehiculoId: Int) {
        viewModelScope.launch {
            repository.getMantenimientosForVehiculo(vehiculoId).collect { mantenimientoList ->
                _mantenimientos.value = mantenimientoList
            }
        }
    }

    // Método para insertar un nuevo mantenimiento
    fun insertMantenimiento(mantenimiento: Mantenimientos) {
        viewModelScope.launch {
            repository.insertMantenimientos(mantenimiento)
            loadMantenimientosForVehiculo(mantenimiento.vehiculo_id)  // Recargar la lista de mantenimientos después de la inserción
        }
    }

    fun updateMantenimiento(mantenimiento: Mantenimientos)  {
        viewModelScope.launch {
            repository.updateMantenimientos(mantenimiento)
            loadMantenimientosForVehiculo(mantenimiento.vehiculo_id)
        }
    }

    fun deleteMantenimiento(mantenimiento: Mantenimientos)  {
        viewModelScope.launch {
            repository.deleteMantenimientos(mantenimiento)
            loadMantenimientosForVehiculo(mantenimiento.vehiculo_id)
        }
    }

}
