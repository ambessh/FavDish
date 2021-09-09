package com.example.favdish.model.database

import androidx.annotation.WorkerThread
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao:FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish:FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList:Flow<List<FavDish>> = favDishDao.getAllDishesList()


    @WorkerThread
    suspend fun updateDishDetailsData(favDish: FavDish){
        favDishDao.updateDishDetails(favDish)
    }

    val getLoveDishData:Flow<List<FavDish>> = favDishDao.getLoveDish()

    @WorkerThread
    suspend fun deleteDishData(favDish: FavDish){
        favDishDao.deleteDish(favDish)
    }

    fun getFilteredListData(value:String) :Flow<List<FavDish>> =favDishDao.getFilteredList(value)


}