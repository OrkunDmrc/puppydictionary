package com.example.puppydictinary.model

import com.google.gson.annotations.SerializedName

data class YandexTr(
    @SerializedName("text")
    val text: String
) {
}