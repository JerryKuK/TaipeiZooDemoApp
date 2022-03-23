package com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.idling.Idling
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val repository: Repository) : ViewModel(){
    val areaData : MutableLiveData<AreaApiModel?> = MutableLiveData()
    val plantData : MutableLiveData<PlantApiModel?> = MutableLiveData()
    val areaError : MutableLiveData<Throwable?> = MutableLiveData()
    val plantError : MutableLiveData<Throwable?> = MutableLiveData()

    val areaDataClick : MutableLiveData<Event<AreaApiModel.Result.AreaDataResult?>> = MutableLiveData()
    val itemUrlClick : MutableLiveData<Event<AreaApiModel.Result.AreaDataResult?>> = MutableLiveData()
    val plantDataClick : MutableLiveData<Event<PlantApiModel.Result.PlantDataResult?>> = MutableLiveData()

    fun getAreaData(): Job{
        //UI Test Used
        Idling.idlingResource.increment()

        return viewModelScope.launch {
            flow {
                val data = repository.getAreaData()
                emit(data)
            }
                .flowOn(Dispatchers.IO)
                .catch {
                    areaError.value = it
                }
                .collect {
                    //UI Test Used
                    Idling.idlingResource.decrement()

                    areaData.value = it
                }
        }
    }

    fun getPlantaData(filter: String): Job{
        //UI Test Used
        Idling.idlingResource.increment()

        return viewModelScope.launch {
            flow {
                val data = repository.getPlantData(filter)
                emit(data)
            }
                .flowOn(Dispatchers.IO)
                .catch { plantError.value = it }
                .collect {
                    //UI Test Used
                    Idling.idlingResource.decrement()

                    plantData.value = it
                }
        }
    }

    fun itemAreaClick(dataResult: AreaApiModel.Result.AreaDataResult){
        areaDataClick.value = Event(dataResult)
    }

    fun itemUrlClick(dataResult: AreaApiModel.Result.AreaDataResult){
        itemUrlClick.value = Event(dataResult)
    }

    fun itemPlantClick(dataResult: PlantApiModel.Result.PlantDataResult){
        plantDataClick.value = Event(dataResult)
    }

    fun clearPlantDataList(){
        plantData.value = null
    }

    fun clearAreaDataList(){
        areaData.value = null
    }
}

class Event<out T>(val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        if (hasBeenHandled) {
            return null
        } else {
            hasBeenHandled = true
            return content
        }
    }
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}