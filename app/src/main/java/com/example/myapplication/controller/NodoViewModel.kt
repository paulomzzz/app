package com.example.myapplication.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.Nodo
import com.example.myapplication.network.RetrofitProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NodoUiState(
    val list: List<Nodo> = emptyList(),
    val isListLoading: Boolean = false,
    val listError: String? = null
)

class NodoViewModel : ViewModel() {

    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }
    private val _state = MutableStateFlow(NodoUiState())
    val state: StateFlow<NodoUiState> = _state.asStateFlow()

    init {
        cargarNodos()
    }

    fun cargarNodos() {
        viewModelScope.launch {
            _state.update { it.copy(isListLoading = true, listError = null) }
            flow { emit(api.obtenerNodos()) }
                .onEach { nodos ->
                    _state.update { it.copy(list = nodos, isListLoading = false) }
                }
                .catch { e ->
                    _state.update { it.copy(listError = e.message, isListLoading = false) }
                }
                .collect()
        }
    }

}
