package com.example.vehixcare

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vehixcare.Database.AppDatabase
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.Screen.ConfiguracionScreen
import com.example.vehixcare.Screen.HomeScreen
import com.example.vehixcare.Screen.MantenimientoScreen
import com.example.vehixcare.Screen.RegisterScreen
import com.example.vehixcare.Screen.VehiculoScreen
import com.example.vehixcare.Screen.SeguroScreen
import com.example.vehixcare.ui.theme.DarkColorScheme
import com.example.vehixcare.ui.theme.LightColorScheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)
        val repository = VehixcareRepository(
            database.userDao(),
            database.vehiculoDao(),
            database.segurosDao(),
            database.mantenimientosDao()
        )
        setContent {
            val navController = rememberNavController()
            var isLoggedIn by remember { mutableStateOf(false) }

            AppNavGraph(
                navController = navController,
                repository = repository,
                isLoggedIn = isLoggedIn,
                onLoginSuccess = { isLoggedIn = true }
            )
        }
    }
}

// Definir color de fondo menta
val colorMenta = Color(0xFF3d5d5d)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehixcareAppTheme(
    showTopBar: Boolean = true,
    navController: NavHostController? = null,
    onLogoutConfirmed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val isDarkThemeSystem = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(isDarkThemeSystem) }
    var selectedColor by remember { mutableStateOf("Light") }

    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = {
            Scaffold(
                topBar = {
                    if (showTopBar) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "VehixCare",
                                    color = Color.White,

                                )
                            },
                            navigationIcon = {
                                navController?.let {
                                    IconButton(onClick = { it.navigateUp() }) {
                                        Icon(
                                            Icons.Filled.ArrowBackIosNew,
                                            contentDescription = "Back",
                                            tint = Color.White
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        Icons.Filled.MoreVert,
                                        contentDescription = "Menu Icon",
                                        tint = Color.White
                                    )
                                }
                                TaskMenu(
                                    expanded = expanded,
                                    onItemClick = { option ->
                                        if (option == "Salir") {
                                            showLogoutDialog = true
                                        } else if (option == "Configuracion") {
                                            navController?.navigate("configuraciones")
                                        }
                                        expanded = false
                                    },
                                    onDismiss = { expanded = false }
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.primary)
                        )
                    }
                },
                        content = { padding ->
                    Surface(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .background(colorScheme.background)
                    ) {
                        content()
                    }
                }
            )

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Cerrar sesión") },
                    text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog = false
                                onLogoutConfirmed()
                            }
                        ) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showLogoutDialog = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (navController != null) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        content()
                    }

                }
            }
        }
    )
}

@Composable
fun TaskMenu(
    expanded: Boolean,
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf("Configuracion", "Salir")

    AnimatedVisibility(
        visible = expanded,
        enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -40 }) + fadeOut()
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onItemClick(option)
                        onDismiss()
                    }
                )
            }
        }
    }
}


@Composable
fun AppNavGraph(
    navController: NavHostController,
    repository: VehixcareRepository,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf("Light") }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home/{userId}" else "start"
    ) {
        composable("home/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            VehixcareAppTheme {
                HomeScreen(navController = navController, repository = repository, userId = userId)
            }
        }

        composable("vehiculo/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            VehixcareAppTheme {
                VehiculoScreen(navController = navController, repository = repository, userId = userId)
            }
        }

        composable("mantenimiento/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull() ?: 0
            VehixcareAppTheme {
                MantenimientoScreen(
                    navController = navController,
                    repository = repository,
                    vehiculoId = vehiculoId
                )
            }
        }

        composable("seguro/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull() ?: 0
            VehixcareAppTheme {
                SeguroScreen(
                    navController = navController,
                    repository = repository,
                    vehiculoId = vehiculoId
                )
            }
        }

        // Ruta para la pantalla de inicio
        composable("start") {
                 StartScreen(navController = navController)
        }

        // Ruta para la pantalla de inicio de sesión
        composable("login") {
                LoginScreen(repository = repository, navController = navController)

        }

        // Ruta para la pantalla de registro
        composable("register") {
                RegisterScreen(navController = navController, repository = repository)

        }

        composable("configuraciones") {
            VehixcareAppTheme {
                ConfiguracionScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeChanged = { newColor ->
                        selectedColor = newColor
                        isDarkTheme = newColor == "Dark"
                    }
                )
            }
        }
    }
}


@Composable
fun StartScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282F32))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen desde recursos
            Image(
                painter = painterResource(id = R.drawable.logo_vehixcure_fondo_negro),
                contentDescription = "Logo de la App",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .padding(16.dp)
            )

            // Botón de Sign In
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 6.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF2DC399),
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(0.dp), // Sin padding para integrar mejor el borde
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text("Sign In")
                }
            }


            // Botón de Register
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 6.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF2DC399),
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Button(
                    onClick = { navController.navigate("register") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(0.dp), // Sin padding para integrar mejor el borde
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1CFFA8), contentColor = Color.Black),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text("Create Account")
                }
            }

        }
    }
}


