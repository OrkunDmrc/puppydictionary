package com.example.puppydictinary.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.puppydictinary.R
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
                Toast.makeText(requireContext(),"you have to choose 'My Language'",Toast.LENGTH_LONG).show()
            }else if(learningLang == ""){
                Toast.makeText(requireContext(),"you have to choose 'I want to learn'",Toast.LENGTH_LONG).show()
            }else{
                var sharedReferences = requireActivity().getSharedPreferences("com.example.puppydictinary.view", Context.MODE_PRIVATE)
                sharedReferences.edit().putString("myLang", myLang).apply()
                sharedReferences.edit().putString("learningLang", learningLang).apply()
                view?.let { Navigation.findNavController(it).navigate(FlagsDirections.actionFlagsToMainMenu()) }
            }
        }
    }
}