package com.example.puppydictinary.model

data class WordViewModel(
    val Id: Int,
    val LangId: Int,
    val DescLangId: Int,
    val Word: String,
    val Phonetic: String?,
    var IsFav: Int,
    var IsLearned: Int,
    val Description: String
    ) {
}