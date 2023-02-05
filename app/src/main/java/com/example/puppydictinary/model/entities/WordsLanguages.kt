package com.example.puppydictinary.model.entities

data class WordsLanguages(
    val WordId: Int,
    val DescLangId: Int,
    val CategoryId: Int,
    val Description: String,
    val IsFav: Boolean,
    val IsLearned: Boolean
) {
}