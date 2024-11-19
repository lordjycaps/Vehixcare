package com.example.vehixcare.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.ViewModel.MantenimientosViewModel
import com.example.vehixcare.ViewModel.MantenimientosViewModelFactory
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.vehixcare.Model.Mantenimientos
import com.example.vehixcare.Model.Vehiculos
import kotlinx.coroutines.launch


@Composable
fun MantenimientoDialog(
    onDismiss: () -> Unit,
    onConfirm: (Mantenimientos) -> Unit,
    vehiculoId: Int
) {
    var tipoMantenimiento by remember { mutableStateOf("") }
    var fechaMantenimiento by remember { mutableStateOf("") }
    var costoMantenimiento by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Mantenimiento") },
        text = {
            Column {
                OutlinedTextField(
                    value = tipoMantenimiento,
                    onValueChange = { tipoMantenimiento = it },
                    label = { Text("Tipo de Mantenimiento") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fechaMantenimiento,
                    onValueChange = { input ->
                        fechaMantenimiento = formatDate(input)
                    },
                    label = { Text("Fecha (dd/mm/yyyy)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = costoMantenimiento,
                    onValueChange = { input ->
                        costoMantenimiento = formatCurrency(input)
                    },
                    label = { Text("Costo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Crear un nuevo objeto Mantenimientos con los datos ingresados
                    val mantenimiento = Mantenimientos(
                        tipo_mantenimiento = tipoMantenimiento,
                        fecha_mantenimiento = fechaMantenimiento,
                        costo_mantenimiento = costoMantenimiento,
                        descripcion = descripcion,
                        vehiculo_id = vehiculoId
                    )
                    onConfirm(mantenimiento) // Llama a la función onConfirm con el nuevo objeto
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun MantenimientoItem(
    mantenimiento: Mantenimientos,
    onEditClick: (Mantenimientos) -> Unit,
    onDeleteClick: (Mantenimientos) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono a la izquierda
            Icon(
                imageVector = Icons.Filled.Build,
                contentDescription = "Mantenimiento",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Contenido de texto
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Tipo de Mantenimiento: ${mantenimiento.tipo_mantenimiento}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Fecha: ${mantenimiento.fecha_mantenimiento}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Costo: ${mantenimiento.costo_mantenimiento}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Descripción: ${mantenimiento.descripcion}",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onEditClick(mantenimiento) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Vehículo")
                    }

                    IconButton(onClick = { onDeleteClick(mantenimiento) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Vehículo",tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MantenimientoScreen(
    navController: NavController,
    repository: VehixcareRepository,
    vehiculoId: Int
) {
    val mantenimientoViewModel: MantenimientosViewModel = viewModel(factory = MantenimientosViewModelFactory(repository))
    var showDialog by remember { mutableStateOf(false) }

    // Lista de mantenimientos
    val mantenimientoList by mantenimientoViewModel.mantenimientos.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var mantenimientoEdit by remember { mutableStateOf<Mantenimientos?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var mantenimientoToDelete by remember { mutableStateOf<Mantenimientos?>(null) }
    var fechaMantenimiento by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Cargar vehículos al iniciar
    LaunchedEffect(Unit) {
        mantenimientoViewModel.loadMantenimientosForVehiculo(vehiculoId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Mantenimiento"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Text(
                "Mantenimientos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(mantenimientoList) { mantenimiento ->
                    MantenimientoItem(
                        mantenimiento = mantenimiento,
                        onEditClick = { selectedMantenimiento ->
                            mantenimientoEdit = selectedMantenimiento
                            isEditing = true
                            showDialog = true
                        },
                        onDeleteClick = { selectedVehiculo ->
                            mantenimientoToDelete = selectedVehiculo
                            showDeleteDialog = true
                        }
                    )
                }
            }

            if (showDialog) {
                MantenimientoDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { mantenimiento ->
                        showDialog = false

                        if (isEditing) {
                            mantenimientoViewModel.updateMantenimiento(mantenimiento)
                        } else {
                            mantenimientoViewModel.insertMantenimiento(mantenimiento)
                        }
                        showDialog = false
                        mantenimientoEdit = null
                    },
                    vehiculoId = vehiculoId
                )
            }

            // Diálogo de confirmación para eliminar
            if (showDeleteDialog && mantenimientoToDelete != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        mantenimientoToDelete = null
                    },
                    title = { Text("Confirmar Eliminación") },
                    text = { Text("¿Estás seguro de que deseas eliminar a ${mantenimientoToDelete!!.tipo_mantenimiento} ${mantenimientoToDelete!!.fecha_mantenimiento}?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    mantenimientoViewModel.deleteMantenimiento(mantenimientoToDelete!!)
                                    showDeleteDialog = false
                                    mantenimientoToDelete = null
                                }
                            }
                        ) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            showDeleteDialog = false
                            mantenimientoToDelete = null
                        }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}


fun formatDate(input: String): String {
    // Filtrar caracteres no deseados
    val sanitizedInput = input.filter { it.isDigit() }

    // Limitar a 8 caracteres (ddmmyyyy)
    val limitedInput = sanitizedInput.take(8)

    // Insertar los separadores de formato
    return buildString {
        for (i in limitedInput.indices) {
            append(limitedInput[i])
            if (i == 1 || i == 3) append("/") // Añadir "/" después de día y mes
        }
    }
}


fun formatCurrency(input: String): String {
    // Elimina cualquier carácter no numérico (excepto puntos y comas)
    val sanitizedInput = input.filter { it.isDigit() }

    if (sanitizedInput.isEmpty()) return ""

    // Convertir el texto en un número entero
    val amount = sanitizedInput.toLongOrNull() ?: 0L

    // Formatear el número como moneda (ej: "1,234.00")
    return "%,d".format(amount).replace(",", ".")
}