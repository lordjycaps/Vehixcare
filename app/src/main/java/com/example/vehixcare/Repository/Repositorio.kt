package com.example.vehixcare.Repository

import com.example.vehixcare.DAO.SegurosDao
import com.example.vehixcare.DAO.UserDao
import com.example.vehixcare.DAO.VechiculosDao
import com.example.vehixcare.DAO.MantenimientosDao
import com.example.vehixcare.Model.Mantenimientos
import com.example.vehixcare.Model.Seguros
import com.example.vehixcare.Model.User
import com.example.vehixcare.Model.Vehiculos
import kotlinx.coroutines.flow.Flow

class VehixcareRepository(
    private val userDao: UserDao,
    private val vehiculoDao: VechiculosDao,
    private val segurosDao: SegurosDao,
    private val mantenimientosDao: MantenimientosDao
) {

    // Users
    fun getUsers() = userDao.getUsers()  // Read
    suspend fun getUserById(id: Int) = userDao.getUserById(id)  // Read by ID
    suspend fun insertUser(user: User) = userDao.insertUser(user)  // Create
    suspend fun updateUser(user: User) = userDao.updateUser(user)  // Update
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)  // Delete    // New: Login validation
    suspend fun getUserByEmail(correo: String): User? {
        return userDao.getUserByEmail(correo)
    }



    // Vehiculo
    fun getVehiculos() = vehiculoDao.getVehiculos()  // Read
    suspend fun getVehiculoById(id: Int) = vehiculoDao.getVehiculoById(id)  // Read by ID
    suspend fun insertVehiculo(vehiculo: Vehiculos) = vehiculoDao.insertVehiculo(vehiculo)  // Create
    suspend fun updateVehiculo(vehiculo: Vehiculos) = vehiculoDao.updateVehiculo(vehiculo)  // Update
    suspend fun deleteVehiculo(vehiculo: Vehiculos) = vehiculoDao.deleteVehiculo(vehiculo)  // Delete
    fun getVehiculosForUser(userId: Int): Flow<List<Vehiculos>> {
        return vehiculoDao.getVehiculosForUser(userId)
    }

    // Seguro
    fun getSeguros() = segurosDao.getSeguros()  // Read
    suspend fun getSeguroById(id: Int) = segurosDao.getSeguroById(id)  // Read by ID
    suspend fun insertSeguro(seguro: Seguros) = segurosDao.insertSeguro(seguro)  // Create
    suspend fun updateSeguro(seguro: Seguros) = segurosDao.updateSeguro(seguro)  // Update
    suspend fun deleteSeguro(seguro: Seguros) = segurosDao.deleteSeguro(seguro )  // Delete
    fun getSegurosForVehiculo(vehiculoId: Int) = segurosDao.getSegurosForVehiculo(vehiculoId)

    // Mantenimientos
    fun getMantenimientoss() = mantenimientosDao.getMantenimientoss()  // Read
    suspend fun getMantenimientosById(id: Int) = mantenimientosDao.getMantenimientosById(id)  // Read by ID
    suspend fun updateMantenimientos(mantenimiento: Mantenimientos) = mantenimientosDao.updateMantenimientos(mantenimiento)  // Update
    suspend fun deleteMantenimientos(mantenimiento: Mantenimientos) = mantenimientosDao.deleteMantenimientos(mantenimiento)  // Delete
    fun getMantenimientosForVehiculo(vehiculoId: Int) = mantenimientosDao.getMantenimientosForVehiculo(vehiculoId)

    suspend fun insertMantenimientos(mantenimiento: Mantenimientos) {
        mantenimientosDao.insertMantenimientos(mantenimiento)
    }
}
