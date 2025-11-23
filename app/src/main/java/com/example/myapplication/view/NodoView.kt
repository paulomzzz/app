package com.example.myapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.controller.NodoViewModel



@Composable
fun NodoView() {
    val viewModel: NodoViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text("📋 Lista de Nodos", style = MaterialTheme.typography.titleLarge)

        when {
            state.isListLoading -> CircularProgressIndicator()
            state.listError != null -> Text(" Error: ${state.listError}", color = Color.Red)
            else -> {


                //      ÚLTIMA LECTURA
                state.list.lastOrNull()?.let { ultimo ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //  TEMPERATURA
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorTemperatura(ultimo.temperatura)
                            ),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🌡️ Temperatura", style = MaterialTheme.typography.titleMedium)
                                Text("${ultimo.temperatura} °C", style = MaterialTheme.typography.bodyLarge)
                            }
                        }

                        //  NIVEL AGUA
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorNivelAgua(ultimo.nivelAgua)
                            ),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("💧 Nivel de Agua", style = MaterialTheme.typography.titleMedium)
                                Text("${ultimo.nivelAgua} cm", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("✨ Última Lectura", style = MaterialTheme.typography.titleMedium)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("🆔 ID: ${ultimo.id}")
                            Text("🏷️ Nombre: ${ultimo.nombre}")
                            Text("📍 Localización: ${ultimo.localizacion}")
                            Text("🌡️ Temperatura: ${ultimo.temperatura}")
                            Text("💧 Nivel Agua: ${ultimo.nivelAgua}")
                            Text("💨 Nivel Humedad: ${ultimo.nivelHumedad}")
                            Text(if (ultimo.operativo) "✅ Operativo" else "⚠️ Inactivo")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("📂 Todos los Nodos", style = MaterialTheme.typography.titleMedium)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(state.list, key = { it.id ?: it.nombre.hashCode() }) { nodo ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("🧩 Nodo: ${nodo.nombre}", style = MaterialTheme.typography.titleMedium)
                                Text("🆔 ID: ${nodo.id}")
                                Text("📍 Localización: ${nodo.localizacion}")
                                Text("🌡️ Temperatura: ${nodo.temperatura}")
                                Text("💧 Nivel Agua: ${nodo.nivelAgua}")
                                Text("💨 Nivel Humedad: ${nodo.nivelHumedad}")
                                Text(if (nodo.operativo) "✅ Operativo" else "⚠️ Inactivo")
                            }
                        }
                    }
                }
            }
        }
    }
}


// COLORES DINÁMICOS


@Composable
fun colorTemperatura(temperatura: Double): Color {
    return when {
        temperatura >= 35 -> Color(0xFFFF5252) // Rojo fuerte (muy alta)
        temperatura <= 5  -> Color(0xFF448AFF) // Azul fuerte (muy baja)
        else -> Color(0xFF90CAF9) // Azul claro (normal)
    }
}

@Composable
fun colorNivelAgua(nivel: Double): Color {
    return when {
        nivel <= 10 -> Color(0xFFFF5252) // Rojo peligro
        nivel >= 80 -> Color(0xFF0D47A1) // Azul oscuro (tanque lleno)
        else -> Color(0xFF64B5F6) // Azul normal
    }
}

