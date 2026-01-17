package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@Composable
fun NuevaTareaRemoteRoute(
    navController: NavController,
    vm: TareasRemoteViewModel, // Recibe el remoto
    modifier: Modifier = Modifier,
) {
    NuevaTareaContent(
        onBack = { navController.popBackStack() },
        onSave = { titulo, descripcion ->
            vm.crearTarea(titulo, descripcion) // Usa la función de la API
            navController.popBackStack()
        }
    )
}