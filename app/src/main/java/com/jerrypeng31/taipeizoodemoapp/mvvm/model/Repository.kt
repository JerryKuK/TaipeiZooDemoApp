package com.jerrypeng31.mvvmtest

import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import com.jerrypeng31.taipeizoodemoapp.retrofit.ApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService){
    companion object{
        const val SCOPE = "resourceAquire"
    }

    suspend fun getAreaData(): AreaApiModel {
        return apiService.areaData(SCOPE)
    }

    suspend fun getPlantData(filter : String): PlantApiModel {
        return apiService.plantData(SCOPE, filter)
    }
}