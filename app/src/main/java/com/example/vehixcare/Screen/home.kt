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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vehixcare.Model.Seguros
import com.example.vehixcare.Model.Vehiculos
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.ViewModel.SegurosViewModel
import com.example.vehixcare.ViewModel.SegurosViewModelFactory
import com.example.vehixcare.ViewModel.VehiculosViewModel
import com.example.vehixcare.ViewModel.VehiculosViewModelFactory


@Composable
fun HomeScreen(navController: NavHostController, repository: VehixcareRepository, userId: Int) {
    val vehiculosViewModel: VehiculosViewModel = viewModel(factory = VehiculosViewModelFactory(repository))
    val vehiculoList by vehiculosViewModel.vehiculos.collectAsState()

    val segurosViewModel: SegurosViewModel = viewModel(factory = SegurosViewModelFactory(repository))
    val segurosList by segurosViewModel.seguros.collectAsState()

    LaunchedEffect(Unit) {
        vehiculosViewModel.loadVehiculosForUser(userId)
        segurosViewModel.loadSegurosForUser(userId)
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, userId = userId)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a VehixCare",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Sección de vehículos
            Text(
                text = "Tus Vehículos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vehiculoList) { vehiculo ->
                    VehiculoCompactCard(vehiculo = vehiculo)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de seguros
            Text(
                text = "Tus Seguros",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(segurosList) { seguro ->
                    SeguroCompactCard(seguro = seguro)
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController, userId: Int) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Vehículos") },
            label = { Text("Vehículos") },
            selected = false,
            onClick = { navController.navigate("vehiculo/$userId") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Build, contentDescription = "Mantenimientos") },
            label = { Text("Mantenimientos") },
            selected = false,
            onClick = { navController.navigate("mantenimiento/$userId") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Shield, contentDescription = "Seguros") },
            label = { Text("Seguros") },
            selected = false,
            onClick = { navController.navigate("seguro/$userId") }
        )
    }
}

@Composable
fun VehiculoCompactCard(vehiculo: Vehiculos) {
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
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
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
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información breve del vehículo
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tipo: ${vehiculo.tipo}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Placa: ${vehiculo.placa}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun SeguroCompactCard(seguro: Seguros) {
    val icono = Icons.Default.Shield
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
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
                contentDescription = "seguro",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información breve del vehículo
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Seguro: ${seguro.aseguradora}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Fecha Vencimiento: ${seguro.fechaVencimiento}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
