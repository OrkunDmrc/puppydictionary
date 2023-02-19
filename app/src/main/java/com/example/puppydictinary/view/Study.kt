package com.example.puppydictinary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel

class Study : Fragment() {
    private lateinit var favoriteWordsListViewModel: FavoriteWordsListViewModel
    private lateinit var words : ArrayList<WordViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_study, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            favoriteWordsListViewModel = FavoriteWordsListViewModel(requireActivity(), StudyArgs.fromBundle(it).myLang, StudyArgs.fromBundle(it).learningLang)
            words = favoriteWordsListViewModel.getFavoriteWordsByIds(StudyArgs.fromBundle(it).wordIds.toTypedArray()) as ArrayList<WordViewModel>
        }
        parentFragmentManager.beginTransaction().replace(R.id.study_frame, StudySelection(words)).commit()
    }
}