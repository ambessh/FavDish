package com.example.favdish.application

import android.app.Application
import com.example.favdish.model.database.FavDishDatabase
import com.example.favdish.model.database.FavDishRepository

class FavDishApplication:Application() {
    private val  database by lazy { FavDishDatabase.getDatabase(this@FavDishApplication) }
     val repository by lazy { FavDishRepository(database.favDishDao()) }
}