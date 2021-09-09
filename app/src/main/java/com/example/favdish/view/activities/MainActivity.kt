package com.example.favdish.view.activities

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.favdish.R
import com.example.favdish.databinding.ActivityMainBinding
import com.example.favdish.model.notification.NotifyWorker
import com.example.favdish.utils.Constants
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navView: BottomNavigationView = binding.navView

         navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dish, R.id.navigation_fav_dish, R.id.navigation_random_dish
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this,navController)

     if(intent.hasExtra(Constants.Notification_id)){
         val notificationId=intent.getIntExtra(Constants.Notification_id,0)
           binding.navView.selectedItemId=R.id.navigation_random_dish
     }

      startNotification()
    }

    fun NotifyConstraint()=Constraints.Builder()
        .setRequiresCharging(false)
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresBatteryNotLow(true)
        .build()

    fun NotifyWork()= PeriodicWorkRequestBuilder<NotifyWorker>(15,TimeUnit.MINUTES)
        .setConstraints(NotifyConstraint())
        .build()

    fun startNotification(){
       WorkManager.getInstance(this).enqueueUniquePeriodicWork(
           "FavDish Notify",
           ExistingPeriodicWorkPolicy.KEEP,
           NotifyWork()
       )
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }

    fun hideBottomNavBar(){
     binding.navView.clearAnimation()
     binding.navView.animate().translationY(binding.navView.height.toFloat()).duration=300
        binding.navView.visibility= View.GONE
    }
    fun showBottomNavBar(){
     binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration=300
        binding.navView.visibility=View.VISIBLE
    }

}