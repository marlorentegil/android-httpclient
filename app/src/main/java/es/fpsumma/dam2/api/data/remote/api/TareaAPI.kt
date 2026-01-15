package es.fpsumma.dam2.api.data.remote.api

import es.fpsumma.dam2.api.data.local.entity.TareaEntity
import es.fpsumma.dam2.api.data.remote.dto.TareaDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TareaAPI {

    @GET("api/tareas")
    suspend fun listar(): Response<List<TareaDTO>>

    @DELETE("api/tareas/{id}")
    suspend fun deleteById(@Path("id") id: Int): Response<Unit>

    @POST("api/tareas")
    suspend fun crearTarea(@Body tarea: TareaDTO): Response<Unit>

    @PUT("api/tareas/{id}")
    suspend fun actualizarTarea(@Path("id") id: Int, @Body tarea: TareaDTO): Response<Unit>

    @GET("api/tareas/{id}")
    suspend fun obtenerDetalle(@Path("id") id: Int): Response<TareaDTO>
}