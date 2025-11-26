package com.example.myapplication.controller

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiService
import com.example.myapplication.data.database.DatabaseProvider
import com.example.myapplication.data.entity.LoginUserEntity
import com.example.myapplication.model.Usuario
import com.example.myapplication.network.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val nombre: String = "",
    val password: String = "",
    val recordarUsuario: Boolean = false,
    val isLoading: Boolean = false,
    val mensaje: String? = null,
    val loginExitoso: Boolean = false
)

class LoginViewModel(
    private val dbProvider: DatabaseProvider = DatabaseProvider
) : ViewModel() {

    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    // 🔹 Cargar usuario guardado al abrir el login
    fun cargarUsuarioGuardado(context: Context) {
        viewModelScope.launch {
            val dao = dbProvider.getDatabase(context).loginUserDao()
            val savedUser = dao.getSavedUser()

            if (savedUser != null) {
                _state.value = _state.value.copy(
                    nombre = savedUser.nombre,
                    password = savedUser.password,
                    recordarUsuario = true
                )
            }
        }
    }

    fun onNombreChange(nuevoNombre: String) {
        _state.value = _state.value.copy(nombre = nuevoNombre)
    }

    fun onPasswordChange(nuevaPassword: String) {
        _state.value = _state.value.copy(password = nuevaPassword)
    }

    fun onRecordarChange(valor: Boolean) {
        _state.value = _state.value.copy(recordarUsuario = valor)
    }

    // 🔹 Login con soporte offline
    fun login(context: Context) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, mensaje = null)

            try {
                val usuario = Usuario(
                    nombre = state.value.nombre,
                    password = state.value.password
                )

                val response = api.login(usuario)

                if (response.isSuccessful) {
                    val mensajeServidor =
                        response.body()?.get("mensaje")?.toString() ?: "Login exitoso"

                    guardarUsuarioSiEsNecesario(context)

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
                // 🔹 Login offline
                val dao = dbProvider.getDatabase(context).loginUserDao()
                val savedUser = dao.getSavedUser()

                if (savedUser != null &&
                    savedUser.nombre == state.value.nombre &&
                    savedUser.password == state.value.password
                ) {
                    _state.value = _state.value.copy(
                        mensaje = "Login offline exitoso",
                        isLoading = false,
                        loginExitoso = true
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        mensaje = "Servidor caído y no existe usuario guardado",
                        loginExitoso = false
                    )
                }
            }
        }
    }

    // 🔹 Guardar usuario si la casilla está marcada
    private suspend fun guardarUsuarioSiEsNecesario(context: Context) {
        if (!state.value.recordarUsuario) return

        val dao = dbProvider.getDatabase(context).loginUserDao()
        dao.deleteAll()

        dao.insert(
            LoginUserEntity(
                id = 1,
                nombre = state.value.nombre,
                password = state.value.password
            )
        )
    }
}
