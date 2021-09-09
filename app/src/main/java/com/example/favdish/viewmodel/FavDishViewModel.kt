package com.example.favdish.viewmodel

import androidx.lifecycle.*
import com.example.favdish.model.database.FavDishRepository
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FavDishViewModel(private val repository: FavDishRepository):ViewModel() {

    fun insert(dish:FavDish)= viewModelScope.launch {

        repository.insertFavDishData(dish)

    }

    val allDishesList:LiveData<List<FavDish>> =repository.allDishesList.asLiveData()

    fun updateData(favDish: FavDish)=viewModelScope.launch {
        repository.updateDishDetailsData(favDish)
    }

    val getLoveDishNow:LiveData<List<FavDish>> = repository.getLoveDishData.asLiveData()

    fun deleteDishNow(favDish: FavDish)=viewModelScope.launch {
        repository.deleteDishData(favDish)
    }

    fun getFilteredNow(value:String):LiveData<List<FavDish>> = repository.getFilteredListData(value).asLiveData()

}

class FavDishViewModelFactory(private val repository: FavDishRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
             return FavDishViewModel(repository) as T
        }
        throw  IllegalArgumentException("unknown viewmodel class")

    }

}