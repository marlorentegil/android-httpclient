package es.fpsumma.dam2.api.viewmodel


import android.R.attr.id
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.fpsumma.dam2.api.data.remote.RetrofitClient
import es.fpsumma.dam2.api.data.remote.dto.TareaDTO
import es.fpsumma.dam2.api.model.Tarea
import es.fpsumma.dam2.api.ui.screen.tareas.TareasUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

/**
 * ViewModel "remoto": obtiene datos desde una API (Retrofit) y expone un estado
 * que la UI (Compose) puede observar.
 *
 * La UI NO llama a Retrofit directamente: solo observa `state`.
 */
class TareasRemoteViewModel : ViewModel() {

    // Cliente Retrofit ya configurado (baseUrl + converter).
    // Aquí obtenemos la interfaz con los endpoints (GET/POST/PUT/DELETE, etc.)
    private val api = RetrofitClient.tareaAPI

    /**
     * Estado interno (mutable) del ViewModel.
     * Lo mantenemos privado para que SOLO el ViewModel pueda modificarlo.
     */
    private val _state = MutableStateFlow(TareasUIState())

    /**
     * Estado público (solo lectura). La UI se suscribe a este StateFlow:
     * - con collectAsState() en Compose
     * - o con collect en otras capas
     */
    val state: StateFlow<TareasUIState> = _state

    /**
     * Carga el listado de tareas desde la API.
     * - Pone loading=true
     * - Llama al endpoint
     * - Si va bien: convierte DTO -> modelo de app (Tarea) y lo guarda en el estado
     * - Si falla: guarda el error en el estado
     */
    fun loadTareas() = viewModelScope.launch {
        // 1) Antes de llamar a la red, avisamos a la UI de que estamos cargando.
        //    También limpiamos errores previos.
        _state.update { current ->
            current.copy(loading = true, error = null)
        }

        /**
         * runCatching hace de "try/catch" funcional:
         * - Lo que devuelva el bloque irá a onSuccess(...)
         * - Si el bloque lanza una excepción, irá a onFailure(...)
         */
        runCatching {
            // 2) Llamada HTTP (suspend) al endpoint: GET /api/tareas
            val res = api.listar()

            // 3) Si el HTTP no es 2xx, lo convertimos en excepción para ir a onFailure(...)
            if (!res.isSuccessful) error("HTTP ${res.code()}")

            // 4) Si el body es null, devolvemos lista vacía (para evitar NPE)
            res.body() ?: emptyList()
        }.onSuccess { listaDto ->
            // 5) Si todo salió bien, aquí tenemos la lista de DTOs que viene de la API.
            //    Convertimos DTO -> Modelo de la app.
            //    (La UI trabaja con Tarea, no con TareaDTO)
            val tareas = listaDto.map { dto ->
                Tarea(
                    id = dto.id,
                    titulo = dto.titulo,
                    descripcion = dto.descripcion
                )
            }

            // 6) Actualizamos el estado:
            //    - tareas con la lista convertida
            //    - loading=false porque ya acabó la carga
            _state.update { current ->
                current.copy(tareas = tareas, loading = false)
            }
        }.onFailure { e ->
            // 7) Si ha fallado (sin internet, timeout, 500, etc.), guardamos un mensaje de error
            //    y paramos el loading.
            _state.update { current ->
                current.copy(
                    error = e.message ?: "Error cargando tareas",
                    loading = false
                )
            }
        }
    }



    fun deleteTareaById() = viewModelScope.launch {

        //Antes de llamar a la red, avisamos a la UI de que estamos cargando
        _state.update { current ->
            current.copy(loading = true, error = null)
        }

        runCatching {

            //Llamada HTTP (suspend) al endpoint: DELETE api/tareas/{id}
            api.deleteById(id)

        }.onSuccess {

            //Lista las tareas, menos la que hemos eliminado
            loadTareas()

        }.onFailure { e ->

            //Si falla y no elimina la tarea
            _state.update { current ->
                current.copy(
                    error = e.message ?: "Error eliminando la tarea",
                    loading = false
                )
            }
        }
    }



    fun crearTarea(titulo: String, descripcion: String) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }

        runCatching {

            // Convertimos los datos de la UI a un DTO para enviarlo a la API
            val nuevaTareaDto = TareaDTO(id = 0, titulo = titulo, descripcion = descripcion)
            val res = api.crearTarea(nuevaTareaDto)

            //Si el HTTP no es 2xx, lo convertimos en excepción para ir a onFailure(...)
            if (!res.isSuccessful) throw Exception("Error al crear: ${res.code()}")

        }.onSuccess {

            // Refresca la lista
            loadTareas()

        }.onFailure { e ->

            //Si falla y no crea la tarea
            _state.update { it.copy(error = e.message ?: "Error creando la tarea", loading = false) }
        }
    }


    fun actualizarTarea() = viewModelScope.launch {

        //Antes de llamar a la red, avisamos a la UI de que estamos cargando
        _state.update { current ->
            current.copy(loading = true, error = null)
        }

        runCatching {

            val actualizarTareaDto = TareaDTO(id = id, titulo = titulo, descripcion = descripcion)
            val res = api.crearTarea(nuevaTareaDto)

            //Llamada HTTP (suspend) al endpoint: DELETE api/tareas/{id}
            api.actualizarTarea(id, tarea: TareaEntity)

        }.onSuccess {

            //Lista las tareas, añadiendo la que hemos creado
            loadTareas()

        }.onFailure { e ->

            //Si falla y no elimina la tarea
            _state.update { current ->
                current.copy(
                    error = e.message ?: "Error eliminando la tarea",
                    loading = false
                )
            }
        }
    }



    private val _selected = MutableStateFlow<Tarea?>(null)
    val selected: StateFlow<Tarea?> = _selected

    fun loadTareaDetalle(id: Int) = viewModelScope.launch {
        runCatching {

            val res = api.obtenerDetalle(id)
            if (!res.isSuccessful) error("HTTP ${res.code()}")
            res.body() ?: error("Sin body")

        }.onSuccess { dto ->

            _selected.value = Tarea(dto.id, dto.titulo, dto.descripcion)

        }.onFailure { e ->

            _state.update { it.copy(error = e.message ?: "Error cargando detalle") }

        }
    }



}