package es.fpsumma.dam2.api.ui.screen.tareas


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import es.fpsumma.dam2.api.ui.navegation.Routes
import es.fpsumma.dam2.api.viewmodel.TareasViewModel


@Composable
fun NuevaTareaRoomRoute(
    navController: NavController,
    vm: TareasViewModel,
    modifier: Modifier = Modifier,
) {
    NuevaTareaContent(
        modifier = modifier,
        onBack = { navController.popBackStack() },
        onSave = { titulo, descripcion ->
            //Guardamos en la base de datos a través del VM
            vm.addTarea(titulo, descripcion)
            //Navegamos hacia atrás o al listado
            navController.navigate(Routes.TAREA_LISTADO)
        }
    )
}
