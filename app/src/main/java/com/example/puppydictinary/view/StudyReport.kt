package com.example.puppydictinary.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puppydictinary.R
import com.example.puppydictinary.adapter.StudyReportRecyclerAdapter
import com.example.puppydictinary.model.WordViewModel
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.fragment_study_report.*
import kotlinx.android.synthetic.main.fragment_study_report.recycler_view


class StudyReport(val wordsList: ArrayList<WordViewModel>, val studyImageViews: ArrayList<ImageView>, val reportList: ArrayList<ArrayList<Boolean>>) : Fragment() {
    private lateinit var recyclerAdapter: StudyReportRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_study_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studyImageViews[0].setBackgroundResource(R.color.button)
        studyImageViews[1].setBackgroundResource(R.color.button)
        studyImageViews[2].setBackgroundResource(R.color.button)
        studyImageViews[3].setBackgroundResource(R.color.card)
        recycler_view.layoutManager = LinearLayoutManager(context)
        var sharedReferences = requireActivity().getSharedPreferences("com.example.puppydictinary.view", Context.MODE_PRIVATE)
        val myLang = sharedReferences.getString("myLang", "")!!
        val learningLang = sharedReferences.getString("learningLang", "")!!
        recyclerAdapter = StudyReportRecyclerAdapter(wordsList, reportList, view.context, view, FavoriteWordsListViewModel(requireActivity(), myLang, learningLang))
        recycler_view.adapter = recyclerAdapter

    }


}