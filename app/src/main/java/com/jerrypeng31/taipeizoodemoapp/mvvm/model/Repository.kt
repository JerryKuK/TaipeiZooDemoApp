package com.jerrypeng31.mvvmtest

import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import com.jerrypeng31.taipeizoodemoapp.retrofit.ApiService
import com.jerrypeng31.taipeizoodemoapp.retrofit.RetrofitUtil
import io.reactivex.Observable
import io.reactivex.Observer

class Repository(private val apiService: ApiService){
    companion object{
        const val SCOPE = "resourceAquire"
    }

    fun getAreaData(): Observable<AreaApiModel> {
        return apiService.areaData(SCOPE)
    }

    fun getPlantData(filter : String): Observable<PlantApiModel> {
        return apiService.plantData(SCOPE, filter)
    }
}