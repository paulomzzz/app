package com.example.myapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun QuienesSomosView(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Somos Platmo, una empresa dedicada al monitoreo medioambiental, con énfasis en el análisis y control de recursos hídricos.",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))


        Text(
            "Este proyecto fue desarrollado por:" +
                    "\nPablo Morales \n Mario Aguilera\n" +
                    "(En memoria de Brandon)",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
