package com.example.puppydictinary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel
import kotlinx.android.synthetic.main.fragment_study.*

class Study : Fragment() {
    private lateinit var favoriteWordsListViewModel: FavoriteWordsListViewModel
    private lateinit var words : ArrayList<WordViewModel>
    private lateinit var studyImageViews: ArrayList<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_study, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Exercise"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        studyImageViews = arrayListOf(selection_study, voice_study, pronunciation_study, check_study)
        arguments?.let {
            favoriteWordsListViewModel = FavoriteWordsListViewModel(requireActivity(), StudyArgs.fromBundle(it).myLang, StudyArgs.fromBundle(it).learningLang)
            words = favoriteWordsListViewModel.getFavoriteWordsByIds(StudyArgs.fromBundle(it).wordIds.toTypedArray()) as ArrayList<WordViewModel>
        }
        selection_study.setBackgroundResource(R.color.empty)
        voice_study.setBackgroundResource(R.color.empty)
        pronunciation_study.setBackgroundResource(R.color.empty)
        check_study.setBackgroundResource(R.color.empty)
        parentFragmentManager.beginTransaction().replace(R.id.study_frame, StudySelection(words, studyImageViews)).commit()
    }
}