package com.example.puppydictinary.service

import com.example.puppydictinary.model.Yandex
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class YandexAPIService {
    private val BASE_URL = "https://dictionary.yandex.net/"
    private val key = ""
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(YandexAPI::class.java)

    fun getData(lang : String, text:String, ui: String) : Single<Yandex> {
        return api.getData( key, lang, ui, text)
    }
}