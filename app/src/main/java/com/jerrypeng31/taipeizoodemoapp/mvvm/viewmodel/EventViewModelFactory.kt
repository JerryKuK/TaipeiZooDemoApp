package com.jerrypeng31.mvvmtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.EventViewModel

class EventViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EventViewModel::class.java) ->
                EventViewModel(repository) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}