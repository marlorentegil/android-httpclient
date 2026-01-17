package es.fpsumma.dam2.api.ui.screen.tareas

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.fpsumma.dam2.api.ui.navegation.Routes
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListadoTareasScreen(
    navController: NavController,
    vm: TareasRemoteViewModel, // Usamos el ViewModel remoto
    modifier: Modifier = Modifier
) {

    // Cogemos el Flow del ViewModel (Observamos el state del RemoteViewModel)
    val uiState by vm.state.collectAsState()
    val tareas = uiState.tareas

    fun handleDeleteTarea(id: Int) {
        vm.deleteTareaById(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de tareas (API)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Row {
                        // Navegamos a la ruta de añadir tareas (versión remota)
                        IconButton(onClick = { navController.navigate(Routes.TAREA_ADD_API) }) {
                            Icon(Icons.AutoMirrored.Filled.NoteAdd, contentDescription = "Añadir")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        // Comprobamos si hay tareas en el estado
        if (tareas.isNotEmpty()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    items = tareas,
                    key = { it.id }
                ) { tarea ->
                    Card(
                        // Mantenemos la navegación de detalle original según tu Routes.kt
                        onClick = { navController.navigate(Routes.tareaView(tarea.id)) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(tarea.titulo) },
                            supportingContent = { Text(tarea.descripcion) },
                            trailingContent = {
                                IconButton(
                                    onClick = { handleDeleteTarea(tarea.id) },
                                    modifier = modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Borrar nota"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        } else {
            // Mantenemos la estructura de la columna para cuando no hay datos
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            ) {
                Row(modifier = modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    // Si está cargando mostramos un texto, si no, el mensaje de vacío
                    Text(if (uiState.loading) "Cargando..." else "No hay tareas aún")
                }
            }
        }
    }
}