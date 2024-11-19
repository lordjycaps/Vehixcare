package com.example.vehixcare.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehixcare.Model.Mantenimientos
import com.example.vehixcare.Model.Seguros
import com.example.vehixcare.Model.Vehiculos
import com.example.vehixcare.Repository.VehixcareRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SegurosViewModel(private val repository: VehixcareRepository) : ViewModel() {

    private val _seguros = MutableStateFlow<List<Seguros>>(emptyList())
    val seguros: StateFlow<List<Seguros>> = _seguros.asStateFlow()

    private val _vehiculos = MutableStateFlow<List<Vehiculos>>(emptyList())
    val vehiculos: StateFlow<List<Vehiculos>> = _vehiculos.asStateFlow()



    fun loadSegurosForVehiculo(vehiculoId: Int) {
        viewModelScope.launch {
            repository.getSegurosForVehiculo(vehiculoId).collect { segurosList ->
                _seguros.value = segurosList
            }
        }
    }

    fun loadVehiculos() {
        viewModelScope.launch {
            repository.getVehiculos().collect { vehiculosList ->
                _vehiculos.value = vehiculosList
            }
        }
    }



    // Método para insertar un nuevo mantenimiento
    fun insertSeguro(seguros: Seguros) {
        viewModelScope.launch {
            repository.insertSeguro(seguros)
            loadSegurosForVehiculo(seguros.vehiculo_id)
        }
    }

    fun updateSeguro(seguros: Seguros)  {
        viewModelScope.launch {
            repository.updateSeguro(seguros)
            loadSegurosForVehiculo(seguros.vehiculo_id)
        }
    }

    fun deleteSeguro(seguros: Seguros)  {
        viewModelScope.launch {
            repository.deleteSeguro(seguros)
            loadSegurosForVehiculo(seguros.vehiculo_id)
        }
    }

    fun loadSegurosForUser(userId: Int) {
        viewModelScope.launch {
            // Obtener los vehículos del usuario
            val vehiculosList = repository.getVehiculosForUser(userId).first() // Usamos `first()` para obtener el valor de la lista

            val segurosList = mutableListOf<Seguros>()

            // Obtener los seguros para cada vehículo de manera asíncrona
            val deferredSeguros = vehiculosList.map { vehiculo ->
                async {
                    repository.getSegurosForVehiculo(vehiculo.id).first() // Obtener los seguros para este vehículo
                }
            }

            // Esperar que se obtengan todos los seguros
            deferredSeguros.awaitAll().forEach { seguros ->
                segurosList.addAll(seguros)
            }

            // Actualizar el estado con la lista completa de seguros
            _seguros.value = segurosList
        }
    }

}