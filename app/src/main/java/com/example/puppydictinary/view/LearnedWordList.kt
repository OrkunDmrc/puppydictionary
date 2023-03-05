package com.example.puppydictinary.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puppydictinary.R
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.puppydictinary.adapter.LearnedWordListRecyclerAdapter
import com.example.puppydictinary.viewmodel.LearnedWordListViewModel
import com.example.puppydictinary.viewmodel.LearnedWordListViewModelFactory
import kotlinx.android.synthetic.main.fragment_main_menu.*

class LearnedWordList : Fragment() {
    private lateinit var viewModel : LearnedWordListViewModel
    private lateinit var viewModelFactory : LearnedWordListViewModelFactory
    private lateinit var recyclerAdapter : LearnedWordListRecyclerAdapter
    private var myLang: String = ""
    private var learningLang: String = ""
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learned_word_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Learned"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        arguments?.let {
            myLang = FavoriteWordsListArgs.fromBundle(it).myLang
            learningLang = FavoriteWordsListArgs.fromBundle(it).learningLang
        }
        viewModelFactory = LearnedWordListViewModelFactory(requireActivity(), myLang, learningLang)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LearnedWordListViewModel::class.java)
        recyclerAdapter = LearnedWordListRecyclerAdapter(arrayListOf(), view.context, requireActivity() as AppCompatActivity, viewModel)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = recyclerAdapter
        viewModel.refreshData()
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