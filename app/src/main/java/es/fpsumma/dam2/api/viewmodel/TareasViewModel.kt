package es.fpsumma.dam2.api.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import es.fpsumma.dam2.api.data.local.AppDatabase
import es.fpsumma.dam2.api.data.local.entity.TareaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import es.fpsumma.dam2.api.model.Tarea
import es.fpsumma.dam2.api.ui.screen.tareas.TareasUIState
import kotlinx.coroutines.flow.map

class TareasViewModel(app: Application) : AndroidViewModel(app) {

    private val db = Room.databaseBuilder(
        app, AppDatabase::class.java, "tareas.db"
    ).fallbackToDestructiveMigration(false).build()

    private val dao = db.tareaDao()

    val state: StateFlow<TareasUIState> =
        dao.getAllTareas()
            .map { lista ->
                TareasUIState(
                    tareas = lista.map { Tarea(it.id, it.titulo, it.descripcion) }
                )
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, TareasUIState())

    fun getTarea(id: Int): StateFlow<TareaEntity?> =
        dao.getTarea(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun addTarea(titulo: String, descripcion: String) = viewModelScope.launch {
        dao.insert(TareaEntity(titulo = titulo, descripcion = descripcion))
    }

    fun updateTarea(id: Int, titulo: String, descripcion: String) = viewModelScope.launch {
        dao.update(TareaEntity(id = id, titulo = titulo, descripcion = descripcion))
    }

    fun deleteTareaById(id: Int) = viewModelScope.launch { dao.deleteById(id) }

}