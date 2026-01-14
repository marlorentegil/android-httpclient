package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun DetalleTareaRoomRoute(
    id: Int,
    navController: NavController,
    vm: TareasViewModel,
    modifier: Modifier = Modifier,
) {
    // Recolectamos el Flow del ViewModel
    val tareaEntity by vm.getTarea(id).collectAsState(initial = null)

    // Si tu ViewModel ya devuelve un objeto Tarea, puedes pasar 'tareaEntity' directamente.
    // Si devuelve una Entity diferente, aquí se hace la conversión.
    DetalleTareaContent(
        tarea = tarea,
        onBack = { navController.popBackStack() },
        onSave = { titulo, descripcion ->
            vm.updateTarea(id, titulo, descripcion)
            navController.popBackStack()
        },
        modifier = modifier
    )
}