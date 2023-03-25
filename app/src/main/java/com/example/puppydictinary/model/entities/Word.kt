package com.example.puppydictinary.model.entities

data class Word(
    val Id: Int,
    val LangId: Int,
    val DescLangId: Int,
    val Word: String,
    val Phonetic: String?,
    var IsFav: Int,
    var IsLearned: Int
) {
}