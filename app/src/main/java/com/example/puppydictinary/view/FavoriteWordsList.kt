package com.example.puppydictinary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puppydictinary.R
import com.example.puppydictinary.adapter.FavoriteWordsListRecyclerAdapter
import com.example.puppydictinary.adapter.ResultWordRecyclerAdapter
import com.example.puppydictinary.service.SQLiteService
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModelFactory
import com.example.puppydictinary.viewmodel.WordListViewModel
import kotlinx.android.synthetic.main.fragment_main_menu.*


class FavoriteWordsList : Fragment() {
    private lateinit var viewModel : FavoriteWordsListViewModel
    private lateinit var viewModelFactory: FavoriteWordsListViewModelFactory
    private lateinit var SQLiteService: SQLiteService
    private val recyclerAdapter = FavoriteWordsListRecyclerAdapter(arrayListOf())
    private var myLang: String = ""
    private var learningLang: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_words_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            myLang = FavoriteWordsListArgs.fromBundle(it).myLang
            learningLang = FavoriteWordsListArgs.fromBundle(it).learningLang
        }
        SQLiteService = SQLiteService(requireActivity(), myLang, learningLang)
        viewModelFactory = FavoriteWordsListViewModelFactory(SQLiteService)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteWordsListViewModel::class.java)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = recyclerAdapter
        viewModel.getFavoriteWordsList()
        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.resultWords.observe(viewLifecycleOwner, Observer{
            it?.let{
                recycler_view.visibility = View.VISIBLE
                recyclerAdapter.updateWordList(it)
            }
        })
    }
}