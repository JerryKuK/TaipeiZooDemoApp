package com.jerrypeng31.taipeizoodemoapp.retrofit

import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/v1/dataset/5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a")
    suspend fun areaData(
        @Query("scope") scope: String
    ): AreaApiModel

    @GET("api/v1/dataset/f18de02f-b6c9-47c0-8cda-50efad621c14")
    suspend fun plantData(
        @Query("scope") scope: String,
        @Query("q") q: String
    ): PlantApiModel
}