package com.example.puppydictinary.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.puppydictinary.model.Word
import com.example.puppydictinary.service.SQLiteService
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel
import com.example.puppydictinary.viewmodel.WordListViewModel
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.result_word_recycler_row.*
import java.util.*

class MainMenu : Fragment() {
    private lateinit var myLang : String
    private lateinit var learningLang : String
    private lateinit var langFrom : String
    private lateinit var langTo : String
    private lateinit var wordListViewModel : WordListViewModel
    private lateinit var favoriteWordsListViewModel: FavoriteWordsListViewModel
    private val words: MutableList<Word> = mutableListOf()

    private val recyclerAdapter = ResultWordRecyclerAdapter(arrayListOf())
    private var tts: TextToSpeech? = null
    private var searchedWord: String = ""
    private var description: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val db = requireActivity().openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
        myLang = sharedReferences.getString("myLang", "")!!
        learningLang = sharedReferences.getString("learningLang", "")!!
        langFrom = sharedReferences.getString("langFrom", learningLang)!!
        langTo = sharedReferences.getString("langTo", myLang)!!
        if(sharedReferences.getBoolean("isItFirst", true)){
            favoriteWordsListViewModel = FavoriteWordsListViewModel(SQLiteService(requireActivity(), myLang, learningLang))
            sharedReferences.edit().putBoolean("isItFirst",false).apply()
            favoriteWordsListViewModel.setFirstAdjust()

            var cursor = db.rawQuery("SELECT * FROM Languages", null)
            var id = cursor.getColumnIndex("Id")
            var name = cursor.getColumnIndex("Name")
            while (cursor.moveToNext()){
                println("-------- Languages -> ID : ${cursor.getInt(id)} Name : ${cursor.getString(name)}")
            }
            cursor = db.rawQuery("SELECT * FROM Categories", null)
            id = cursor.getColumnIndex("Id")
            name = cursor.getColumnIndex("Name")
            while (cursor.moveToNext()){
                println("-------- Categories -> ID : ${cursor.getInt(id)} Name : ${cursor.getString(name)}")
            }
            cursor = db.rawQuery("SELECT * FROM Words", null)
            id = cursor.getColumnIndex("Id")
            var langId = cursor.getColumnIndex("LangId")
            var catId = cursor.getColumnIndex("CategoryId")
            var word = cursor.getColumnIndex("Word")
            while (cursor.moveToNext()){
                println("-------- Words -> ID : ${cursor.getInt(id)} LangId : ${cursor.getInt(langId)} catId : ${cursor.getInt(catId)} word : ${cursor.getString(word)}")
            }
            cursor = db.rawQuery("SELECT * FROM WordsLanguages", null)
            id = cursor.getColumnIndex("WordId")
            langId = cursor.getColumnIndex("DescLangId")
            var desc = cursor.getColumnIndex("Description")
            var isFav = cursor.getColumnIndex("IsFav")
            var isLearned = cursor.getColumnIndex("IsLearned")
            while (cursor.moveToNext()){
                println("-------- WordsLanguages -> WordID : ${cursor.getInt(id)} DescLangId : ${cursor.getInt(langId)} desc : ${cursor.getString(desc)} IsFav : ${cursor.getString(isFav)} IsLearned : ${cursor.getString(isLearned)}")
            }
            cursor.close()
            view?.let { Navigation.findNavController(it).navigate(MainMenuDirections.actionMainMenuToFlags()) }
        }else{
            favoriteWordsListViewModel = FavoriteWordsListViewModel(SQLiteService(requireActivity(), myLang, learningLang))
            var cursor = db.rawQuery("SELECT w.Id, w.Word, wl.Description FROM WordsLanguages as wl join Words as w on w.Id = wl.WordId",null)
            val id = cursor.getColumnIndex("Id")
            val word = cursor.getColumnIndex("Word")
            val desc = cursor.getColumnIndex("Description")
            while (cursor.moveToNext()){
                println("-------- WordsLanguages Words -> WordID : ${cursor.getInt(id)} word : ${cursor.getString(word)} desc : ${cursor.getString(desc)}")
            }
            cursor = db.rawQuery("SELECT * FROM Languages", null)
            var id2 = cursor.getColumnIndex("Id")
            var name2 = cursor.getColumnIndex("Name")
            var code = cursor.getColumnIndex("Code")
            while (cursor.moveToNext()){
                println("-------- Languages -> ID : ${cursor.getInt(id2)} Name : ${cursor.getString(name2)} Code : ${cursor.getString(code)}")
            }
            cursor.close()

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
                tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                    if (it == TextToSpeech.SUCCESS) {
                        tts?.language = Locale.US
                        tts?.setSpeechRate(1.0f)
                        tts?.speak(searchedWord, TextToSpeech.QUEUE_ADD, null,"")
                    }
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
                    if(true/*SQLiteService.isFavoriteWord(searchWord)*/){
                        add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
                    }
                }
            }
            add_remove_favorite_button.setOnClickListener{
                if(favoriteWordsListViewModel.isFavoriteWord(searchedWord)){
                    favoriteWordsListViewModel.removeWordFavorites(searchedWord)
                    add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border_24)
                }else{
                    favoriteWordsListViewModel.addWordFavorites(words)
                    add_remove_favorite_button.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
                }
            }
            gecis_deneme_button.setOnClickListener {
                val action = MainMenuDirections.actionMainMenuToFavoriteWordsList(myLang, learningLang)
                Navigation.findNavController(it).navigate(action)
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
                for(item in recyclerAdapter.wordList){
                    description = ""
                    for(desc in item.tr){
                        description += desc.text + ", "
                    }
                    words.add(Word(0, favoriteWordsListViewModel.findCategoryIdByName(item.pos), item.pos, item.text, item.ts, description))
                }
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