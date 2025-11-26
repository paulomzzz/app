package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current

    // abre la pantalla → carga el usuario guardado
    LaunchedEffect(Unit) {
        viewModel.cargarUsuarioGuardado(context)
    }

    // Navegar después del login
    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) {
            delay(1500)
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
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.platmo),
            contentDescription = "Logo de Platmo",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 CASILLA "Recordar usuario"
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.recordarUsuario,
                    onCheckedChange = { viewModel.onRecordarChange(it) }
                )
                Text("Recordar usuario")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(context) },
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

            TextButton(onClick = { navController.navigate("registro") }) {
                Text("Crear cuenta")
            }

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
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("qr") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escanear QR del GitHub")
            }

        }
    }
}
