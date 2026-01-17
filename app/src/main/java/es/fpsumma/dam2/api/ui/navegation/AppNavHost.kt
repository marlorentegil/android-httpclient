package es.fpsumma.dam2.api.ui.navegation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.fpsumma.dam2.api.ui.screen.tareas.DetalleTareaRemoteRoute
import es.fpsumma.dam2.api.ui.screen.tareas.DetalleTareaRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasScreen
import es.fpsumma.dam2.api.ui.screen.tareas.NuevaTareaRemoteRoute
import es.fpsumma.dam2.api.ui.screen.tareas.NuevaTareaRoomRoute
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    tareasViewModel: TareasViewModel // ViewModel para Room (Local)
) {
    // Instanciamos el ViewModel remoto para las llamadas a la API
    val remoteVm: TareasRemoteViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.TAREA_LISTADO_API // Punto de inicio según tu instrucción
    ) {

        // --- RUTAS DE LA API (REMOTO) ---

        // Listado principal desde la API
        composable(Routes.TAREA_LISTADO_API) {
            ListadoTareasScreen(navController, remoteVm)
        }

        // Añadir tarea mediante la API (Usa la versión RemoteRoute)
        composable(Routes.TAREA_ADD_API) {
            NuevaTareaRemoteRoute(navController, remoteVm)
        }

        // Detalle y edición mediante la API (Usa la versión RemoteRoute)
        composable(
            route = Routes.TAREA_VIEW_API,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            DetalleTareaRemoteRoute(
                id = id,
                navController = navController,
                vm = remoteVm // Pasamos remoteVm a DetalleTareaRemoteRoute
            )
        }

        // --- RUTAS DE ROOM (LOCAL) ---

        composable(Routes.TAREA_LISTADO) {
            ListadoTareasRoomRoute(navController, tareasViewModel)
        }

        composable(Routes.TAREA_ADD) {
            NuevaTareaRoomRoute(navController, tareasViewModel)
        }

        composable(
            route = Routes.TAREA_VIEW,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            DetalleTareaRoomRoute(
                id = id,
                navController = navController,
                vm = tareasRoomViewModel // Aquí pasamos el ViewModel de Room
            )
        }
    }
}