package com.example.puppydictinary.model

import com.google.gson.annotations.SerializedName

data class YandexDef(
    @SerializedName("text")
    val text: String,
    @SerializedName("pos")
    val pos: String,
    @SerializedName("ts")
    val ts: String,
    @SerializedName("tr")
    val tr: List<YandexTr>
) {
}