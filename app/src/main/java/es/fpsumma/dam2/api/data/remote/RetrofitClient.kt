package es.fpsumma.dam2.api.data.remote

import es.fpsumma.dam2.api.data.remote.api.TareaAPI
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:3000/" // 👈 cambia puerto

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val tareaAPI: TareaAPI by lazy {
        retrofit.create(TareaAPI::class.java)
    }
}