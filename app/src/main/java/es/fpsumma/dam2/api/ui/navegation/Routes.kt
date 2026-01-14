package es.fpsumma.dam2.api.ui.navegation

object Routes {
    const val HOME = "home"
    const val TAREA_LISTADO = "tareas/listado"
    const val TAREA_LISTADO_API = "tareas/listadoapi"

    const val TAREA_ADD = "tareas/nueva"

    const val TAREA_VIEW = "tareas/detalle/{id}"

    fun tareaView(id: Int): String {
        return "tareas/detalle/$id"
    }

}