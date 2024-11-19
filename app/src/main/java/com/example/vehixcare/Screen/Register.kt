package com.example.vehixcare.Screen

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vehixcare.Model.User
import com.example.vehixcare.Repository.VehixcareRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


val colorPrincipal = Color(0xFF282F32)
val colorPrincipalOscuro = Color(0xFF4C997F)
val colorSecundario = Color.Black

@Composable
fun RegisterScreen(navController: NavController, repository: VehixcareRepository, ) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var apellidoUsuario by remember { mutableStateOf("") }
    var fecha_nacimiento by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showFormError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro de Usuario",
                fontSize = 30.sp,
                color = colorSecundario,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo Nombre de Usuario
            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Nombres") },
                singleLine = true,
                isError = nombreUsuario.isBlank() && showFormError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            //Campo Apellido
            OutlinedTextField(
                value = apellidoUsuario,
                onValueChange = { apellidoUsuario = it },
                label = { Text("Apellidos") },
                singleLine = true,
                isError = apellidoUsuario.isBlank() && showFormError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            //Campo fecha nacimiento
            OutlinedTextField(
                value = fecha_nacimiento,
                onValueChange = { fecha_nacimiento = formatFecha(it) },
                label = { Text("Fecha Nacimiento") },
                singleLine = true,
                isError = fecha_nacimiento.isBlank() && showFormError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            // Campo de Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                isError = correo.isBlank() && showFormError,
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
                isError = password.isBlank() && showFormError,
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

            // Campo de Confirmar Contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmPassword.isBlank() && showFormError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            // Botón de Registro
            Button(
                onClick = {
                    if (correo.isNotBlank() && password == confirmPassword && nombreUsuario.isNotBlank() && apellidoUsuario.isNotBlank() && fecha_nacimiento.isNotBlank()) {
                        val user = User(
                            nombre = nombreUsuario,
                            apellido = apellidoUsuario,
                            fecha_nacimiento = fecha_nacimiento,
                            correo = correo,
                            password = confirmPassword
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            // Si no estamos en edición, agregamos el autor
                            repository.insertUser(user)

                            // Limpiamos los campos y volvemos al estado inicial
                            withContext(Dispatchers.Main) {
                                nombreUsuario = ""
                                apellidoUsuario = ""
                                fecha_nacimiento = ""
                                correo = ""
                                confirmPassword = ""
                                showFormError = false
                            }
                        }
                    } else {
                        showFormError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorPrincipalOscuro),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "Registrarse", color = colorSecundario)
            }

            if (showFormError) {
                Text(
                    text = "Por favor, completa los campos correctamente.",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            TextButton(
                onClick = {
                    navController.navigate("login")
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .align(alignment = Alignment.CenterVertically)
                ) {
                    Text(text = "¿Ya tienes cuenta?", color = Color.Black)
                    Text(text = " Ingresa aquí", color = colorPrincipal)

                }
            }
        }
    }
}

fun formatFecha(fecha: String): String {
    val cleanedInput = fecha.filter { it.isDigit() }
    val sb = StringBuilder()

    cleanedInput.forEachIndexed { index, c ->
        if (index == 2 || index == 4) sb.append('/')
        sb.append(c)
    }

    return if (sb.length <= 10) sb.toString() else sb.substring(0, 10)
}


