package com.example.vehixcare.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vehixcare.Model.Vehiculos
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.ViewModel.VehiculosViewModel
import com.example.vehixcare.ViewModel.VehiculosViewModelFactory
import kotlinx.coroutines.launch


@Composable
fun VehiculoScreen(
    navController: NavController,
    repository: VehixcareRepository,
    userId: Int
) {
    val vehiculosViewModel: VehiculosViewModel = viewModel(factory = VehiculosViewModelFactory(repository))
    val vehiculoList by vehiculosViewModel.vehiculos.collectAsState()  // Estado de la lista de vehículos

    var showDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var vehiculoEdit by remember { mutableStateOf<Vehiculos?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var vehiculoToDelete by remember { mutableStateOf<Vehiculos?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        vehiculosViewModel.loadVehiculosForUser(userId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isEditing = false
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Vehículo"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Vehículos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(vehiculoList) { vehiculo ->
                    VehiculoCard(
                        vehiculo = vehiculo,
                        onEditClick = { selectedVehiculo ->
                            vehiculoEdit = selectedVehiculo
                            isEditing = true
                            showDialog = true
                        },
                        onDeleteClick = { selectedVehiculo ->
                            vehiculoToDelete = selectedVehiculo
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Diálogo de creación/edición
    if (showDialog) {
        VehiculoDialog(
            vehiculo = if (isEditing) vehiculoEdit else null,
            onDismiss = { showDialog = false },
            onConfirm = { vehiculo ->
                if (isEditing) {
                    vehiculosViewModel.updateVehiculo(vehiculo)
                } else {
                    vehiculosViewModel.insertVehiculo(vehiculo)
                }
                showDialog = false
                vehiculoEdit = null
            },
            userId = userId
        )
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog && vehiculoToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                vehiculoToDelete = null
            },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${vehiculoToDelete!!.tipo} ${vehiculoToDelete!!.modelo}?") },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            vehiculosViewModel.deleteVehiculo(vehiculoToDelete!!)
                            showDeleteDialog = false
                            vehiculoToDelete = null
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDeleteDialog = false
                    vehiculoToDelete = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}



@Composable
fun VehiculoDialog(
    vehiculo: Vehiculos? = null,
    onDismiss: () -> Unit,
    onConfirm: (Vehiculos) -> Unit,
    userId: Int
) {
    var modelo by remember { mutableStateOf(vehiculo?.modelo ?: "") }
    var color by remember { mutableStateOf(vehiculo?.color ?: "") }
    var placa by remember { mutableStateOf(vehiculo?.placa ?: "") }
    var tipo by remember { mutableStateOf(vehiculo?.tipo ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (vehiculo == null) "Nuevo Vehículo" else "Editar Vehículo") },
        text = {
            Column {
                OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") })
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") })
                OutlinedTextField(value = placa, onValueChange = { placa = it }, label = { Text("Placa") })
                TipoDropdown(
                    tipoSeleccionado = tipo,
                    onTipoSeleccionadoChange = { seleccionado ->
                        tipo = seleccionado
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedVehiculo = Vehiculos(
                    id = vehiculo?.id ?: 0,  // Si es edición, conserva el ID; si no, será 0 para autogenerarse
                    modelo = modelo,
                    color = color,
                    placa = placa,
                    tipo = tipo,
                    user_id = userId
                )
                onConfirm(updatedVehiculo)
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun VehiculoCard(
    vehiculo: Vehiculos,
    onEditClick: (Vehiculos) -> Unit,
    onDeleteClick: (Vehiculos) -> Unit
) {
    val icono = when (vehiculo.tipo.uppercase()) {
        "MOTO" -> Icons.AutoMirrored.Filled.DirectionsBike
        "CARRO" -> Icons.Filled.DirectionsCar
        "CAMIONETA" -> Icons.Filled.DirectionsCar
        "CAMIÓN" -> Icons.Filled.LocalShipping
        else -> Icons.AutoMirrored.Filled.Help
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono dinámico
            Icon(
                imageVector = icono,
                contentDescription = "Tipo de vehículo",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Tipo: ${vehiculo.tipo}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Color: ${vehiculo.color}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Modelo: ${vehiculo.modelo}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Placa: ${vehiculo.placa}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Botones de acción
            Row(horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { onEditClick(vehiculo) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Vehículo",
                    )
                }

                IconButton(onClick = { onDeleteClick(vehiculo) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Vehículo",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


@Composable
fun TipoDropdown(
    tipoSeleccionado: String,
    onTipoSeleccionadoChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Controla si el menú está expandido
    val opciones = listOf("MOTO", "CARRO", "CAMIONETA", "CAMIÓN")

    Column {
        OutlinedTextField(
            value = tipoSeleccionado,
            onValueChange = {},
            label = { Text("Tipo") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true, // Hace que el TextField sea solo para lectura
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar menú")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    onClick = {
                        onTipoSeleccionadoChange(opcion)
                        expanded = false
                    },
                    text = { Text(opcion) }
                )
            }
        }
    }
}
