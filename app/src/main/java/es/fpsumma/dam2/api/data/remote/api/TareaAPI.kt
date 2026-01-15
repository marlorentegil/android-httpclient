package es.fpsumma.dam2.api.data.remote.api

import es.fpsumma.dam2.api.data.remote.dto.TareaDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET

interface TareaAPI {

    @GET("api/tareas")
    suspend fun listar(): Response<List<TareaDTO>>

    @DELETE("api/tareas/{id}")
    suspend fun deleteById(id: Int)


}