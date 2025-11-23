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

data class LoginUiState(
    val nombre: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val mensaje: String? = null,
    val loginExitoso: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onNombreChange(nuevoNombre: String) {
        _state.value = _state.value.copy(nombre = nuevoNombre)
    }

    fun onPasswordChange(nuevaPassword: String) {
        _state.value = _state.value.copy(password = nuevaPassword)
    }

    fun login() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, mensaje = null)
            try {
                val usuario = Usuario(state.value.nombre, state.value.password)
                val response = api.login(usuario)

                if (response.isSuccessful) {
                    val mensajeServidor = response.body()?.get("mensaje")?.toString() ?: "Login exitoso"
                    _state.value = _state.value.copy(
                        isLoading = false,
                        mensaje = mensajeServidor,
                        loginExitoso = true
                    )
                } else {
                    val mensajeError = response.errorBody()?.string() ?: "Error desconocido"
                    _state.value = _state.value.copy(
                        isLoading = false,
                        mensaje = mensajeError,
                        loginExitoso = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    mensaje = "Error al conectar con servidor",
                    loginExitoso = false
                )
            }
        }
    }
}
