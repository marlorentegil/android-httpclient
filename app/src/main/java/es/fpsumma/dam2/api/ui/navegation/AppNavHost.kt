package es.fpsumma.dam2.api.ui.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.fpsumma.dam2.api.ui.screen.tareas.DetalleTareaRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.DetalleTareaScreen
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasScreen
import es.fpsumma.dam2.api.ui.screen.tareas.NuevaTareaRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.NuevaTareaScreen
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun AppNavHost(navController: NavHostController, tareasViewModel: TareasViewModel) {
    NavHost(navController = navController, startDestination = Routes.TAREA_LISTADO_API) {
        composable(Routes.TAREA_LISTADO) { ListadoTareasRoomRoute(navController, tareasViewModel) }
        composable(Routes.TAREA_ADD) { NuevaTareaRoomRoute(navController, tareasViewModel) }
        composable(
            route = Routes.TAREA_VIEW,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStage ->
            DetalleTareaRoomRoute(
                id = backStage.arguments?.getInt("id") ?: 0,
                navController,
                tareasViewModel
            )
        }

    }
}