package com.example.myapplication.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.Usuario
import com.example.myapplication.network.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val nombre: String = "",
    val password: String = "",
    // campos extra solo para UI (no se envían al backend)
    val email: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val nota: String = "",
    val isLoading: Boolean = false,
    val mensaje: String? = null,
    val registroExitoso: Boolean = false
)

class RegisterViewModel : ViewModel() {

    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    // Campos principales
    fun onNombreChange(nuevoNombre: String) {
        _state.value = _state.value.copy(nombre = nuevoNombre)
    }

    fun onPasswordChange(nuevaPassword: String) {
        _state.value = _state.value.copy(password = nuevaPassword)
    }

    // Campos extra (solo UI)
    fun onEmailChange(nuevoEmail: String) {
        _state.value = _state.value.copy(email = nuevoEmail)
    }

    fun onTelefonoChange(nuevoTelefono: String) {
        _state.value = _state.value.copy(telefono = nuevoTelefono)
    }

    fun onDireccionChange(nuevaDireccion: String) {
        _state.value = _state.value.copy(direccion = nuevaDireccion)
    }

    fun onNotaChange(nuevaNota: String) {
        _state.value = _state.value.copy(nota = nuevaNota)
    }

    // Registrar: solo envia nombre y password
    fun registrar() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, mensaje = null, registroExitoso = false)
            try {
                // Construimos el usuario sólo con nombre y password
                val usuario = Usuario(_state.value.nombre, _state.value.password)
                val response = api.registrarUsuario(usuario)

                if (response.isSuccessful) {
                    val body = response.body()
                    val mensajeServidor = body?.get("mensaje")?.toString() ?: "Usuario registrado"
                    _state.value = _state.value.copy(
                        isLoading = false,
                        mensaje = mensajeServidor,
                        registroExitoso = mensajeServidor.contains("correctamente", ignoreCase = true)
                    )
                } else {
                    val errorText = response.errorBody()?.string()
                    val mensajeError = errorText ?: "Error ${response.code()}"
                    _state.value = _state.value.copy(
                        isLoading = false,
                        mensaje = mensajeError,
                        registroExitoso = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    mensaje = "Error de conexión: ${e.message}",
                    registroExitoso = false
                )
            }
        }
    }
}
