package es.fpsumma.dam2.api.ui.navegation

object Routes {
    const val HOME = "home"
    const val TAREA_LISTADO = "tareas/listado"
    const val TAREA_LISTADO_API = "tareas/listadoapi"

    // Rutas Locales
    const val TAREA_ADD = "tareas/nueva"
    const val TAREA_VIEW = "tareas/detalle/{id}"

    // --- NUEVAS RUTAS REMOTAS ---
    const val TAREA_ADD_API = "tareas/nueva_api"
    const val TAREA_VIEW_API = "tareas/detalle_api/{id}"

    fun tareaView(id: Int) = "tareas/detalle/$id"

}