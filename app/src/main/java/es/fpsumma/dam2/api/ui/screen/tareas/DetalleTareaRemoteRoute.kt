package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@Composable
fun DetalleTareaRemoteRoute(
    id: Int,
    navController: NavController,
    vm: TareasRemoteViewModel, // Recibe el remoto
    modifier: Modifier = Modifier,
) {
    val tarea by vm.selected.collectAsState() //

    LaunchedEffect(id) {
        vm.loadTareaDetalle(id) // Carga desde la API
    }

    // El error "Argument type mismatch" se va al usar este check de nulabilidad
    tarea?.let { tareaSegura ->
        DetalleTareaContent(
            tarea = tareaSegura,
            onBack = { navController.popBackStack() },
            onSave = { titulo, descripcion ->
                vm.actualizarTarea(id, titulo, descripcion)
                navController.popBackStack()
            }
        )
    }
}