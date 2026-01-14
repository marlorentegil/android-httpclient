package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import es.fpsumma.dam2.api.ui.navegation.Routes
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@Composable
fun ListadoTareasRemoteRoute(
    vm: TareasRemoteViewModel = viewModel(),
    navController: NavController,
    modifier: Modifier = Modifier,
){
    //Observamos el estado del ViewModel (StateFlow -> State de Compose)
    val state by vm.state.collectAsState()

    //LaunchedEffect(Unit) se ejecuta una sola vez al entrar en la pantalla
    // Llama a la función para cargar las tareas desde la API
    LaunchedEffect(Unit) {
        vm.loadTareas()
    }

    // 3. Pasamos el UIState al Content
    // Según las instrucciones, onDelete se pasa vacío: {}
    ListadoTareasContent(
        state = state,
        onBack = { navController.popBackStack() },
        onAdd = { navController.navigate(Routes.TAREA_ADD) },
        onOpenDetalle = { id -> navController.navigate(Routes.tareaView(id)) },
        modifier = modifier,
        onDelete = {}
    )

}



