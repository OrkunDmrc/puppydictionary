package com.example.puppydictinary.model

data class Word(
    val Id: Int,
    val CategoryId: Int,
    val CategoryName: String,
    val Word: String,
    val Phonetic: String,
    val Description: String
) {
}