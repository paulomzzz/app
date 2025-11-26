package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.*
import com.example.myapplication.view.QrScannerScreen   // ✔ ESTE ES EL CORRECTO

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlatmoApp()
        }
    }
}

@Composable
fun PlatmoApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") { LoginView(navController) }

        composable("registro") { RegisterView(navController) }

        composable("quienes") { QuienesSomosView(navController) }

        composable("nodos") { NodoView() }

        // 🔥 ESTA ES LA RUTA CORRECTA PARA TU CÓDIGO
        composable("qr") {
            QrScannerScreen(
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}
