package com.example.puppydictinary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.puppydictinary.R
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    companion object{
        fun findLangByCode(learningLang: String) : Locale {
            return when(learningLang){
                "tr" -> Locale("tr", "TR")
                "en" -> Locale.US
                "de" -> Locale("de","DE")
                else -> Locale.US
            }
        }
        fun findLangById(learningLangId: Int) : Locale {
            return when(learningLangId){
                1 -> Locale("tr", "TR")
                2 -> Locale.US
                3 -> Locale("de","DE")
                else -> Locale.US
            }
        }
        fun findSpeechVoiceById(learningLangId: Int) : String {
            return when(learningLangId){
                1 -> "tr-TR"
                2 -> "en-US"
                3 -> "de-DE"
                else -> "en-US"
            }
        }
    }
}





































