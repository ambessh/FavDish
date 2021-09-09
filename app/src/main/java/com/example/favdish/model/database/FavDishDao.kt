package com.example.favdish.model.database

import androidx.room.*
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow


@Dao
interface FavDishDao {

  @Insert
  suspend fun insertFavDishDetails(favDish:FavDish)

  @Query("SELECT * FROM fav_dishes_table ORDER BY id")
  fun getAllDishesList(): Flow<List<FavDish>>

  @Update
  suspend fun updateDishDetails(favDish: FavDish)



  @Query("SELECT * FROM fav_dishes_table WHERE favourite_dish = 1 ")
  fun getLoveDish():Flow<List<FavDish>>

  @Delete
  suspend fun deleteDish(favDish: FavDish)

  @Query("SELECT * FROM fav_dishes_table WHERE type = :filterType")
  fun getFilteredList(filterType:String):Flow<List<FavDish>>
}