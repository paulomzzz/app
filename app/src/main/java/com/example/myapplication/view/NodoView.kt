package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Lista de Nodos", style = MaterialTheme.typography.titleLarge)

        if (state.isListLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.listError != null) {
            Text("Error: ${state.listError}")
        } else {
            // Último nodo
            state.list.lastOrNull()?.let { ultimo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Último Nodo", style = MaterialTheme.typography.titleMedium)
                        Text("ID: ${ultimo.id}")
                        Text("Nombre: ${ultimo.nombre}")
                        Text("Localización: ${ultimo.localizacion}")
                        Text("Temperatura: ${ultimo.temperatura}")
                        Text("Nivel Agua: ${ultimo.nivelAgua}")
                        Text("Nivel Humedad: ${ultimo.nivelHumedad}")
                        Text("Operativo: ${ultimo.operativo}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(state.list, key = { it.id ?: it.nombre.hashCode() }) { nodo ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("ID: ${nodo.id}")
                            Text("Nombre: ${nodo.nombre}")
                            Text("Localización: ${nodo.localizacion}")
                            Text("Temperatura: ${nodo.temperatura}")
                            Text("Nivel Agua: ${nodo.nivelAgua}")
                            Text("Nivel Humedad: ${nodo.nivelHumedad}")
                            Text("Operativo: ${nodo.operativo}")
                        }
                    }
                }
            }
        }
    }
}

