package com.example.favdish.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.favdish.model.entities.RandomDish
import com.example.favdish.model.network.RandomDishApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class RandomDIshViewModel : ViewModel() {
    private val randomDishAPiService=RandomDishApiService()
    private val compositeDisposable=CompositeDisposable()

    val loadRandomDish=MutableLiveData<Boolean>()
    val randomDishResponse=MutableLiveData<RandomDish.Recipes>()
    val loadRandomDishError=MutableLiveData<Boolean>()

    fun getRandomDishFromApi(){
        compositeDisposable.add(
            randomDishAPiService.getRandomDish()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<RandomDish.Recipes>(){
                    override fun onSuccess(value: RandomDish.Recipes) {
                        loadRandomDish.value=true
                        randomDishResponse.value=value
                        loadRandomDishError.value=false
                    }

                    override fun onError(e: Throwable) {
                        loadRandomDish.value=false
                        loadRandomDishError.value=true
                        e.printStackTrace()
                    }

                })
        )
    }


}