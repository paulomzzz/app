package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.controller.LoginViewModel
import kotlinx.coroutines.delay
import com.example.myapplication.R

@Composable
fun LoginView(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel()
    val state by viewModel.state.collectAsState()


// Navega tras login exitoso
    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) {
            delay(3000)
            navController.navigate("nodos") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // 👈 logo arriba
    ) {
        Spacer(modifier = Modifier.height(100.dp)) // margen superior

        //  Logo de Platmo arriba del formulario
        Image(
            painter = painterResource(id = R.drawable.platmo),
            contentDescription = "Logo de Platmo",
            modifier = Modifier

                .padding(bottom = 32.dp)
        )

        // Centra el resto del contenido más abajo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Iniciar Sesión", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Loguear")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para crear cuenta
            TextButton(onClick = { navController.navigate("registro") }) {
                Text("Crear cuenta")
            }

            // Botón para “Quiénes somos”
            TextButton(onClick = { navController.navigate("quienes") }) {
                Text("Quiénes somos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.mensaje?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = if (state.loginExitoso) Color(0xFF2E7D32) else Color.Red
                )
            }
        }
    }


}
