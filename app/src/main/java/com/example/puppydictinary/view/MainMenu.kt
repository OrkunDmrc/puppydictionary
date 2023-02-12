package com.example.puppydictinary.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puppydictinary.R
import com.example.puppydictinary.adapter.ResultWordRecyclerAdapter
import com.example.puppydictinary.model.YandexDef
import com.example.puppydictinary.model.entities.Word
import com.example.puppydictinary.service.sqliteservice.SQLiteService
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel
import com.example.puppydictinary.viewmodel.WordListViewModel
import kotlinx.android.synthetic.main.fragment_main_menu.*
import java.util.*

class MainMenu : Fragment() {
    private lateinit var myLang : String
    private lateinit var learningLang : String
    private lateinit var langFrom : String
    private lateinit var langTo : String
    private lateinit var wordListViewModel : WordListViewModel
    private lateinit var favoriteWordsListViewModel: FavoriteWordsListViewModel
    private lateinit var yandexDef : List<YandexDef>

    private val words: MutableList<Word> = mutableListOf()

    private val recyclerAdapter = ResultWordRecyclerAdapter(arrayListOf())
    private var tts: TextToSpeech? = null
    private var searchedWord: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.fav_words_button -> {
                val action = MainMenuDirections.actionMainMenuToFavoriteWordsList(myLang, learningLang)
                Navigation.findNavController(requireView()).navigate(action)
                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sharedReferences = requireActivity().getSharedPreferences("com.example.puppydictinary.view", Context.MODE_PRIVATE)
        //val db = requireActivity().openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
        myLang = sharedReferences.getString("myLang", "")!!
        learningLang = sharedReferences.getString("learningLang", "")!!
        langFrom = sharedReferences.getString("langFrom", learningLang)!!
        langTo = sharedReferences.getString("langTo", myLang)!!
        favoriteWordsListViewModel = FavoriteWordsListViewModel(requireActivity(), myLang, learningLang)
        if(sharedReferences.getBoolean("isItFirst", true)){
            sharedReferences.edit().putBoolean("isItFirst",false).apply()
            favoriteWordsListViewModel.setFirstAdjusts()
            view?.let { Navigation.findNavController(it).navigate(MainMenuDirections.actionMainMenuToFlags()) }
        }else{
            if(myLang == "" || learningLang == ""){
                view.let { Navigation.findNavController(it).navigate(MainMenuDirections.actionMainMenuToFlags()) }
            }
            (activity as AppCompatActivity).supportActionBar?.show()
            (activity as AppCompatActivity).supportActionBar?.title = ""
            setDictionaryFlags(langFrom, langTo)
            change_dictionary_flags.setOnClickListener{
                sharedReferences.edit().putString("langFrom", langTo).apply()
                sharedReferences.edit().putString("langTo", langFrom).apply()
                langFrom = sharedReferences.getString("langFrom", "")!!
                langTo = sharedReferences.getString("langTo", "")!!
                setDictionaryFlags(langFrom, langTo)
            }
            pronunciation_button.setOnClickListener {
                pronunciation_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_volume_down_24)
                tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                    if (it == TextToSpeech.SUCCESS) {
                        tts?.language = Locale.US
                        tts?.setSpeechRate(1.0f)
                        tts?.speak(searchedWord, TextToSpeech.QUEUE_ADD, null,"")
                    }
                    pronunciation_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_volume_up_24)
                })
            }
            wordListViewModel = ViewModelProvider(this).get(WordListViewModel::class.java)
            recycler_view.layoutManager = LinearLayoutManager(context)
            recycler_view.adapter = recyclerAdapter
            wordListViewModel.firstData()
            observeLiveData()
            search_button.setOnClickListener{
                val searchWord = search_src_text.text.toString().trim().lowercase()
                wordListViewModel.refreshData(langFrom, langTo, searchWord)
                observeLiveData()
                add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border_24)
                if(favoriteWordsListViewModel.isFavoriteWord(searchWord)){
                    add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
                }
            }
            search_button2.setOnClickListener {
                if(paragraph_edit_text.text.isNullOrEmpty()){
                    Toast.makeText(context, "you choose nothing",Toast.LENGTH_LONG).show()
                }else{
                    val searchWord = paragraph_edit_text.text!!.substring(paragraph_edit_text.selectionStart, paragraph_edit_text.selectionEnd).trim().lowercase()
                    wordListViewModel.refreshData(langFrom, langTo, searchWord)
                    observeLiveData()
                    if(favoriteWordsListViewModel.isFavoriteWord(searchWord)){
                        add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
                    }
                }
            }
            add_remove_favorite_button.setOnClickListener{
                if(favoriteWordsListViewModel.isFavoriteWord(searchedWord)){
                    favoriteWordsListViewModel.removeWordFavorites(searchedWord)
                    add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border_24)
                }else{
                    favoriteWordsListViewModel.addWordFavorites(yandexDef)
                    add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
                }
            }
        }
    }

    private fun setDictionaryFlags(langFrom: String, langTo: String){
        when (langFrom) {
            "en" -> {
                lang_from.setImageResource(R.mipmap.ic_england_flag)
            }
            "de" ->
            {
                lang_from.setImageResource(R.mipmap.ic_germany_flag)
            }
            "tr" ->
            {
                lang_from.setImageResource(R.mipmap.ic_turkey_flag)
            }
            else -> {
                lang_from.setImageResource(R.color.empty)
            }
        }
        when (langTo) {
            "en" -> {
                lang_to.setImageResource(R.mipmap.ic_england_flag)
            }
            "de" ->
            {
                lang_to.setImageResource(R.mipmap.ic_germany_flag)
            }
            "tr" ->
            {
                lang_to.setImageResource(R.mipmap.ic_turkey_flag)
            }
            else -> {
                lang_to.setImageResource(R.color.empty)
            }
        }
    }

    private fun observeLiveData(){
        wordListViewModel.resultWords.observe(viewLifecycleOwner,Observer{
            it?.let {
                recycler_view.visibility = View.VISIBLE
                recyclerAdapter.updateWordList(it)
                words.clear()
                yandexDef = it
                searchedWord = yandexDef[0].text
            }
        })
        wordListViewModel.resultErrorMessage.observe(viewLifecycleOwner,Observer{
            it?.let{
                if(it){
                    error_message.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                    progress_circular.visibility = View.GONE
                    word_information.visibility = View.GONE
                }else{
                    error_message.visibility = View.GONE
                }
            }
        })
        wordListViewModel.progressCircular.observe(viewLifecycleOwner,Observer{
            it?.let{
                if(it){
                    progress_circular.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                    error_message.visibility = View.GONE
                    word_information.visibility = View.GONE
                }else{
                    progress_circular.visibility = View.GONE
                }
            }
        })
        wordListViewModel.wordInformation.observe(viewLifecycleOwner,Observer{
            it?.let{
                if(it){
                    word_information.visibility = View.VISIBLE
                    recycler_view.visibility = View.VISIBLE
                    error_message.visibility = View.GONE
                    progress_circular.visibility = View.GONE
                }else{
                    word_information.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}