package com.example.favdish.model.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favdish.model.entities.FavDish

@Database(entities = [FavDish::class],version = 1)
public abstract class FavDishDatabase():RoomDatabase() {

    abstract  fun favDishDao():FavDishDao

    companion object {
        @Volatile
        private var INSTANCE: FavDishDatabase? = null

        fun getDatabase(context: Context): FavDishDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishDatabase::class.java,
                    "fav_dish_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}