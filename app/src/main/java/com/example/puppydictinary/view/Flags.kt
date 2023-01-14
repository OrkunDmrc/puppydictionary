package com.example.puppydictinary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        myLangOpEn.setOnClickListener {
            myLangImageView.setBackgroundResource(R.mipmap.ic_england_flag)
        }
        myLangOpDe.setOnClickListener {
            myLangImageView.setBackgroundResource(R.mipmap.ic_germany_flag)
        }
        myLangOpTr.setOnClickListener {
            myLangImageView.setBackgroundResource(R.mipmap.ic_turkiye_flag)
        }
        learningLangOpEn.setOnClickListener {
            learningLangImageView.setBackgroundResource(R.mipmap.ic_england_flag)
        }
        learningLangOpDe.setOnClickListener {
            learningLangImageView.setBackgroundResource(R.mipmap.ic_germany_flag)
        }
        learningLangOpTr.setOnClickListener {
            learningLangImageView.setBackgroundResource(R.mipmap.ic_turkiye_flag)
        }
        return inflater.inflate(R.layout.fragment_flags, container, false)
    }
}