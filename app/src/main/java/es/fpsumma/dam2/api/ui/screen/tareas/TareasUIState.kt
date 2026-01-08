package es.fpsumma.dam2.api.ui.screen.tareas

import es.fpsumma.dam2.api.model.Tarea

data class TareasUIState (
    val tareas: List<Tarea> = emptyList(), //listado de tareas
    val loading: Boolean = false, //muestra si las tareas se cargan o no
    val error: String? = null //Muestra si hay un error para informar al usuario
)