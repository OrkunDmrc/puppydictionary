package com.example.puppydictinary.model

import com.google.gson.annotations.SerializedName

data class Yandex(
    @SerializedName("def")
    val def: List<YandexDef>
) {
}