package com.example.myapplication.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.DatabaseProvider
import com.example.myapplication.data.repository.NodoRepository
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.api.ApiService
import com.example.myapplication.model.Nodo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * NodoUiState
 * - estado simple para la UI (lista, loading, error)
 */
data class NodoUiState(
    val list: List<Nodo> = emptyList(),
    val isListLoading: Boolean = false,
    val listError: String? = null
)

/**
 * NodoViewModel (AndroidViewModel)
 *
 * - Extiende AndroidViewModel para disponer de applicationContext (necesario para Room)
 * - Crea el Repository (api + dao) y expone un StateFlow con el estado.
 * - En init intenta sincronizar (fetchAndSaveNodos), si falla lee datos locales y muestra mensaje.
 */
class NodoViewModel(application: Application) : AndroidViewModel(application) {

    // ApiService usando tu RetrofitProvider
    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }

    // Dao obtenido desde DatabaseProvider (singleton)
    private val dao = DatabaseProvider.getDatabase(application.applicationContext).nodoDao()

    // Repositorio que une API + DAO
    private val repository: NodoRepository by lazy { NodoRepository(api, dao) }

    // Estado interno y público
    private val _state = MutableStateFlow(NodoUiState())
    val state: StateFlow<NodoUiState> = _state.asStateFlow()

    init {
        // Cargar nodos al iniciar
        cargarNodos()
        // Además podríamos observar repository.nodos para actualizaciones automáticas si queremos:
        // observeLocalNodos()
    }

    /**
     * cargarNodos()
     * - Intenta sincronizar desde la API y guardar en BD.
     * - Si la llamada falla, lee los nodos locales y actualiza el estado con mensaje.
     */
    fun cargarNodos() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isListLoading = true, listError = null)
            try {
                // Intentar actualizar desde la API (y guardar localmente)
                repository.fetchAndSaveNodos()

                // Luego leer los nodos locales (Flow.first() para obtener el valor actual)
                val nodosLocal = repository.nodos.first() // obtiene la lista actual desde Room
                _state.value = _state.value.copy(list = nodosLocal, isListLoading = false, listError = null)
            } catch (e: Exception) {
                // Si falla la red o la sincronización, tratamos de devolver lo que haya en local
                try {
                    val nodosLocal = repository.nodos.first()
                    _state.value = _state.value.copy(
                        list = nodosLocal,
                        isListLoading = false,
                        listError = "Cargado desde cache: ${e.message}"
                    )
                } catch (dbEx: Exception) {
                    _state.value = _state.value.copy(
                        list = emptyList(),
                        isListLoading = false,
                        listError = "Error al cargar nodos: ${e.message}; Cache: ${dbEx.message}"
                    )
                }
            }
        }
    }

    /**
     * (Opcional) Observa cambios locales continuos y actualiza _state automáticamente.
     * Útil si quieres que la UI reciba updates en tiempo real desde Room sin llamar cargarNodos().
     */
    private fun observeLocalNodos() {
        viewModelScope.launch {
            repository.nodos
                .catch { e ->
                    // Si flow falla, informar error en estado
                    _state.value = _state.value.copy(listError = e.message)
                }
                .collect { lista ->
                    _state.value = _state.value.copy(list = lista)
                }
        }
    }
}
