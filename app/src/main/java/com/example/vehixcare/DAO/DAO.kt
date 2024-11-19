package com.example.vehixcare.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vehixcare.Model.Mantenimientos
import com.example.vehixcare.Model.Seguros
import com.example.vehixcare.Model.User
import com.example.vehixcare.Model.Vehiculos
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)  // Create

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<User>>  // Read

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User  // Read by ID

    @Update
    suspend fun updateUser(user: User)  // Update

    @Delete
    suspend fun deleteUser(user: User)  // Delete

    // Método para obtener un usuario por correo electrónico
    @Query("SELECT * FROM users WHERE correo = :correo LIMIT 1")
    suspend fun getUserByEmail(correo: String): User?
}


@Dao
interface VechiculosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehiculo(vehiculo: Vehiculos)  // Create

    @Query("SELECT * FROM vehiculos")
    fun getVehiculos(): Flow<List<Vehiculos>>  // Read

    @Query("SELECT * FROM vehiculos WHERE id = :id")
    suspend fun getVehiculoById(id: Int): Vehiculos  // Read by ID

    @Update
    suspend fun updateVehiculo(vehiculo: Vehiculos)  // Update

    @Delete
    suspend fun deleteVehiculo(vehiculo: Vehiculos)  // Delete

    @Query("SELECT * FROM vehiculos WHERE user_id = :userId")
    fun getVehiculosForUser(userId: Int): Flow<List<Vehiculos>>


}


@Dao
interface SegurosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeguro(seguro: Seguros)  // Create

    @Query("SELECT * FROM seguros")
    fun getSeguros(): Flow<List<Seguros>>  // Read

    @Query("SELECT * FROM seguros WHERE id = :id")
    suspend fun getSeguroById(id: Int): Seguros  // Read by ID

    @Update
    suspend fun updateSeguro(seguro: Seguros)  // Update

    @Delete
    suspend fun deleteSeguro(seguro: Seguros)  // Delete

    @Query("SELECT * FROM seguros WHERE vehiculo_id = :vehiculoId")
    fun getSegurosForVehiculo(vehiculoId: Int): Flow<List<Seguros>>
}


@Dao
interface MantenimientosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMantenimientos(mantenimiento: Mantenimientos)  // Create

    @Query("SELECT * FROM mantenimientos")
    fun getMantenimientoss(): Flow<List<Mantenimientos>>  // Read

    @Query("SELECT * FROM mantenimientos WHERE id = :id")
    suspend fun getMantenimientosById(id: Int): Mantenimientos  // Read by ID

    @Update
    suspend fun updateMantenimientos(mantenimiento: Mantenimientos)  // Update

    @Delete
    suspend fun deleteMantenimientos(mantenimiento: Mantenimientos)  // Delete

    @Query("SELECT * FROM mantenimientos WHERE vehiculo_id = :vehiculoId")
    fun getMantenimientosForVehiculo(vehiculoId: Int): Flow<List<Mantenimientos>>
}
