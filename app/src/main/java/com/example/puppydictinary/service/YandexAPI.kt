package com.example.puppydictinary.service

import com.example.puppydictinary.model.Yandex
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YandexAPI {
    @GET("api/v1/dicservice.json/lookup")
    fun getData(@Query("key") key: String, @Query("lang") lang: String, @Query("ui") ui: String, @Query("text") text: String) : Single<Yandex>
}