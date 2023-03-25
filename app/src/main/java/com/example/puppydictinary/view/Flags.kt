package com.example.puppydictinary.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.puppydictinary.R
import com.example.puppydictinary.model.entities.Category
import com.example.puppydictinary.model.entities.Language
import com.example.puppydictinary.service.sqliteservice.CategoryService
import com.example.puppydictinary.service.sqliteservice.LanguageService
import kotlinx.android.synthetic.main.fragment_flags.*

class Flags : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedColor = getResources().getColor(R.color.button)
        val unselectedColor = getResources().getColor(R.color.text)
        var myLang = ""
        var learningLang = ""
        (activity as AppCompatActivity).supportActionBar?.hide()
        myLangOpEn.setOnClickListener {
            myLangOpEn.setBackgroundColor(selectedColor)
            myLangOpDe.setBackgroundColor(unselectedColor)
            myLangOpTr.setBackgroundColor(unselectedColor)
            myLang = "en"
        }
        myLangOpDe.setOnClickListener {
            myLangOpDe.setBackgroundColor(selectedColor)
            myLangOpEn.setBackgroundColor(unselectedColor)
            myLangOpTr.setBackgroundColor(unselectedColor)
            myLang = "de"
        }
        myLangOpTr.setOnClickListener {
            myLangOpTr.setBackgroundColor(selectedColor)
            myLangOpDe.setBackgroundColor(unselectedColor)
            myLangOpEn.setBackgroundColor(unselectedColor)
            myLang = "tr"
        }
        learningLangOpEn.setOnClickListener {
            learningLangOpEn.setBackgroundColor(selectedColor)
            learningLangOpDe.setBackgroundColor(unselectedColor)
            learningLangOpTr.setBackgroundColor(unselectedColor)
            learningLang = "en"
        }
        learningLangOpDe.setOnClickListener {
            learningLangOpDe.setBackgroundColor(selectedColor)
            learningLangOpEn.setBackgroundColor(unselectedColor)
            learningLangOpTr.setBackgroundColor(unselectedColor)
            learningLang = "de"
        }
        learningLangOpTr.setOnClickListener {
            learningLangOpTr.setBackgroundColor(selectedColor)
            learningLangOpDe.setBackgroundColor(unselectedColor)
            learningLangOpEn.setBackgroundColor(unselectedColor)
            learningLang = "tr"
        }
        nextButton.setOnClickListener{
            if(myLang == ""){
                Toast.makeText(requireContext(), "${getString(R.string.mustChoose)} '${getString(R.string.myLang)}'",Toast.LENGTH_SHORT).show()
            }else if(learningLang == ""){
                Toast.makeText(requireContext(),"${getString(R.string.mustChoose)} '${getString(R.string.learnLang)}'",Toast.LENGTH_SHORT).show()
            }else{
                var sharedReferences = requireActivity().getSharedPreferences("com.example.puppydictinary.view", Context.MODE_PRIVATE)
                sharedReferences.edit().putString("myLang", myLang).apply()
                sharedReferences.edit().putString("learningLang", learningLang).apply()
                sharedReferences.edit().putString("langFrom", learningLang).apply()
                sharedReferences.edit().putString("langTo", myLang).apply()
                val _db = requireActivity().openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
                val langService = LanguageService(_db, myLang, learningLang)
                val myLangId = langService.getIdByCode(myLang)
                if(langService.getIdByCode(myLang) == 0)
                    langService.add(setLang(myLang))
                val catService = CategoryService(_db, myLangId, langService.getIdByCode(learningLang))
                if(!catService.haveLangId(myLangId)){
                    for(item in setCat(myLang, myLangId)){
                        catService.add(item)
                    }
                }
                view?.let { Navigation.findNavController(it).navigate(FlagsDirections.actionFlagsToMainMenu()) }
            }
        }
    }

    private fun setLang(code: String) : Language{
        return when(code){
            "tr" -> Language(0,code,"Türkçe")
            "en" -> Language(0,code,"İngilizce")
            "de" -> Language(0,code,"Almanca")
            else -> Language(0,code,"")
        }
    }

    private fun setCat(code: String, langId: Int) : ArrayList<Category>{
        return when(code){
            "tr" -> {
                arrayListOf<Category>(
                    Category(0,langId,"diğer"),
                    Category(0,langId,"fiil"),
                    Category(0,langId,"isim"),
                    Category(0,langId,"sıfat"),
                    Category(0,langId,"zamir"),
                    Category(0,langId,"zarf"),
                    Category(0,langId,"edat"),
                    Category(0,langId,"bağlaç")
                )
            }
            "en" -> {
                arrayListOf<Category>(
                    Category(0,langId,"another"),
                    Category(0,langId,"verb"),
                    Category(0,langId,"noun"),
                    Category(0,langId,"adjective"),
                    Category(0,langId,"pronoun"),
                    Category(0,langId,"adverb"),
                    Category(0,langId,"preposition"),
                    Category(0,langId,"conjunction")
                )
            }
            "de" -> {
                arrayListOf<Category>(
                    Category(0,langId,"andere"),
                    Category(0,langId,"Verb"),
                    Category(0,langId,"Substantiv"),
                    Category(0,langId,"Adjektiv"),
                    Category(0,langId,"Pronomen"),
                    Category(0,langId,"Adverb"),
                    Category(0,langId,"Präposition"),
                    Category(0,langId,"Konjunktion")
                )
            }
            else -> arrayListOf<Category>()
        }
    }
}