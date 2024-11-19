package com.example.vehixcare.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val fecha_nacimiento: String,
    val correo: String,
    val password: String

)

@Entity(
    tableName = "vehiculos",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"])]
)
data class Vehiculos(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val modelo: String,
    val color: String,
    val placa: String,
    val tipo: String,//carro o moto
    val user_id: Int
)

@Entity(
    tableName = "seguros",
    foreignKeys = [
        ForeignKey(
            entity = Vehiculos::class,
            parentColumns = ["id"],
            childColumns = ["vehiculo_id"],
            onDelete = ForeignKey.CASCADE
        ),

    ],
    indices = [
        Index(value = ["vehiculo_id"]),
    ]
)
data class Seguros(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vehiculo_id: Int,
    val aseguradora: String,
    val numeroPoliza: String,
    val fechaVencimiento: String
)

@Entity(
    tableName = "mantenimientos",
    foreignKeys = [
        ForeignKey(
            entity = Vehiculos::class,
            parentColumns = ["id"],
            childColumns = ["vehiculo_id"],
            onDelete = ForeignKey.CASCADE
        ),

    ],
    indices = [
        Index(value = ["vehiculo_id"]),
    ]
)
data class Mantenimientos(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo_mantenimiento: String,
    val fecha_mantenimiento	: String,
    val costo_mantenimiento	: String,
    val descripcion	: String,
    val vehiculo_id: Int,
)