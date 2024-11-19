import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vehixcare.Repository.VehixcareRepository
import com.example.vehixcare.ViewModel.AuthViewModel

// Definir los colores
val colorPrincipal = Color(0xFF282F32)
val colorPrincipalOscuro = Color(0xFF4C997F)
val colorSecundario = Color.Black


@Composable
fun LoginScreen(
    repository: VehixcareRepository,
    navController: NavController
) {
    // Inicializamos AuthViewModel utilizando AuthViewModelFactory
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val currentUser by authViewModel.currentUser.collectAsState()

    val loginSuccess by authViewModel.loginSuccess.collectAsState()
    val showLoginError by authViewModel.showLoginError.collectAsState()
    var showFormError by remember { mutableStateOf(false) }


    // Observa el cambio de loginSuccess y navega si es exitoso
    LaunchedEffect(loginSuccess) {
        if (loginSuccess && currentUser != null) {
            val userId = currentUser!!.id
            navController.navigate("home/$userId") {
                popUpTo("login") { inclusive = true }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Centrar contenido vertical y horizontalmente
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // Para que ocupe todo el ancho disponible
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrar elementos dentro de la columna
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 30.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Campo de Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            Button(
                onClick = {
                    if (correo.isNotBlank() && password.isNotBlank()) {
                        authViewModel.login(correo, password)
                        showFormError = false // Resetear el error si los campos están completos
                    } else {
                        showFormError = true // Mostrar mensaje de error si algún campo está vacío
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorPrincipalOscuro),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "Iniciar Sesión", color = colorSecundario)
            }

            // Mensaje de error en caso de fallo
            if (showLoginError) {
                Text(
                    text = "Correo o contraseña incorrectos",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (showFormError) {
                Text(
                    text = "Por favor, completa todos los campos.",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Botón para ir a la pantalla de registro
            TextButton(
                onClick = {
                    navController.navigate("register")
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "¿No tienes cuenta?", color = Color.Black)
                    Text(text = " Regístrate aquí", color = colorPrincipal)
                }
            }
        }
    }
}
