package com.example.vehixcare.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vehixcare.DAO.MantenimientosDao
import com.example.vehixcare.DAO.SegurosDao
import com.example.vehixcare.DAO.UserDao
import com.example.vehixcare.DAO.VechiculosDao
import com.example.vehixcare.Model.Mantenimientos
import com.example.vehixcare.Model.Seguros
import com.example.vehixcare.Model.User
import com.example.vehixcare.Model.Vehiculos

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregar la columna "duracion" a la tabla "seguros"
        database.execSQL("ALTER TABLE seguros ADD COLUMN duracion INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(entities = [User::class, Vehiculos::class, Seguros::class, Mantenimientos::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun vehiculoDao(): VechiculosDao
    abstract fun segurosDao(): SegurosDao
    abstract fun mantenimientosDao(): MantenimientosDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vehixcare_database"
                )
                    .addMigrations(MIGRATION_2_3) // Registrar la migración
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}