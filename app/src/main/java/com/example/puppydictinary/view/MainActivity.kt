package com.example.puppydictinary.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.example.puppydictinary.R
import com.example.puppydictinary.model.Yandex
import com.example.puppydictinary.service.YandexAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sharedReferences = this.getSharedPreferences("com.example.puppydictinary.view", Context.MODE_PRIVATE)
        if(sharedReferences.getBoolean("isItFirst", true)){

        }else{

        }

    }
}





































