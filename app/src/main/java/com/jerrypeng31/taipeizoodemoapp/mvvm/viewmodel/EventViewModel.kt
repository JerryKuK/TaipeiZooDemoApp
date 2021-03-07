package com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventViewModel(private val repository: Repository) : ViewModel(){
    val areaDataList : MutableLiveData<Event<AreaApiModel?>> = MutableLiveData()
    val plantDataList : MutableLiveData<Event<PlantApiModel?>> = MutableLiveData()
    val error : MutableLiveData<Event<Throwable?>> = MutableLiveData()

    val areaDataClick : MutableLiveData<Event<AreaApiModel.Result.AreaDataResult?>> = MutableLiveData()
    val itemUrlClick : MutableLiveData<Event<AreaApiModel.Result.AreaDataResult?>> = MutableLiveData()
    val plantDataClick : MutableLiveData<Event<PlantApiModel.Result.PlantDataResult?>> = MutableLiveData()

    fun getAreaData(): Disposable{
        return repository.getAreaData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { areaDataList.value = Event(it) },
                { error.value = Event(it) }
            )
    }

    fun getPlantaData(filter: String): Disposable{
        return repository.getPlantData(filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { plantDataList.value = Event(it) },
                { error.value = Event(it) }
            )
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
        plantDataList.value = null
    }

    fun clearAreaDataList(){
        areaDataList.value = null
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