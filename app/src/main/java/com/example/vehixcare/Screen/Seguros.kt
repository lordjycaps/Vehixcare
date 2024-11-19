package com.example.vehixcare.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vehixcare.Model.Seguros
import com.example.vehixcare.Model.Vehiculos
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.ViewModel.SegurosViewModel
import com.example.vehixcare.ViewModel.SegurosViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun SeguroScreen(
    navController: NavController,
    repository: VehixcareRepository,
    vehiculoId: Int
) {
    val segurosViewModel: SegurosViewModel = viewModel(factory = SegurosViewModelFactory(repository))
    val vehiculosList by segurosViewModel.vehiculos.collectAsState()
    val segurosList by segurosViewModel.seguros.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showForm by remember { mutableStateOf(false) }  // Estado para mostrar/ocultar el formulario
    var isEditing by remember { mutableStateOf(false) }
    var segurosEdit by remember { mutableStateOf<Seguros?>(null) }
    var selectedVehiculoId by remember { mutableStateOf(vehiculoId) }



    LaunchedEffect(Unit) {
        segurosViewModel.loadSegurosForVehiculo(vehiculoId)
    }

    LaunchedEffect(Unit) {
        segurosViewModel.loadVehiculos()
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showForm = true }, // Mostrar formulario al hacer clic
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Seguro")
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                Text("Seguros", style = MaterialTheme.typography.titleLarge)

                AnimatedVisibility(visible = showForm) {
                    SegurosForm(
                        vehiculosList = vehiculosList,
                        onDismiss = { showForm = false },
                        onConfirm = { seguros ->
                            if (isEditing) {
                                segurosViewModel.updateSeguro(seguros.copy(vehiculo_id = selectedVehiculoId))
                            } else {
                                segurosViewModel.insertSeguro(seguros.copy(vehiculo_id = selectedVehiculoId))
                            }
                            showForm = false // Ocultar formulario después de guardar
                            isEditing = false
                            segurosEdit = null
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(segurosList) { seguros ->
                        SegurosCard(
                            seguros = seguros,
                            onEditClick = {
                                segurosEdit = it
                                isEditing = true
                                selectedVehiculoId = it.vehiculo_id // Cargar el vehiculo_id al editar
                                showForm = true
                            },
                            onDeleteClick = {
                                // Implementar lógica para eliminar
                            }
                        )
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegurosForm(
    vehiculosList: List<Vehiculos>,
    onDismiss: () -> Unit,
    onConfirm: (Seguros) -> Unit
) {
    var aseguradora by remember { mutableStateOf("") }
    var numeroPoliza by remember { mutableStateOf("") }
    var fechaVencimiento by remember { mutableStateOf("") }
    var selectedVehiculoId by remember { mutableStateOf(vehiculosList.firstOrNull()?.id ?: 0) }

    // Estado de expansión para el dropdown
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = aseguradora,
            onValueChange = { aseguradora = it },
            label = { Text("Aseguradora") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = numeroPoliza,
            onValueChange = { numeroPoliza = it },
            label = { Text("Número de Póliza") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fechaVencimiento,
            onValueChange = { fechaVencimiento = it },
            label = { Text("Fecha de Vencimiento") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown para seleccionar el vehículo
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = vehiculosList.find { it.id == selectedVehiculoId }?.placa ?: "Selecciona un vehículo",
                onValueChange = {},
                label = { Text("Vehículo") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vehiculosList.forEach { vehiculo ->
                    DropdownMenuItem(
                        text = { Text(vehiculo.placa) },
                        onClick = {
                            selectedVehiculoId = vehiculo.id
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (aseguradora.isNotEmpty() && numeroPoliza.isNotEmpty() && fechaVencimiento.isNotEmpty()) {

                        onConfirm(
                            Seguros(
                                aseguradora = aseguradora,
                                numeroPoliza = numeroPoliza,
                                fechaVencimiento = fechaVencimiento,
                                vehiculo_id = selectedVehiculoId
                            )
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar Seguro")
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
fun SegurosCard(
    seguros: Seguros,
    onEditClick: (Seguros) -> Unit,
    onDeleteClick: (Seguros) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DirectionsCar,
                contentDescription = "Seguro",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Aseguradora: ${seguros.aseguradora}", style = MaterialTheme.typography.bodyMedium)
                Text("Fecha Vencimiento: ${seguros.fechaVencimiento}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onEditClick(seguros) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = { onDeleteClick(seguros) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Seguro", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


