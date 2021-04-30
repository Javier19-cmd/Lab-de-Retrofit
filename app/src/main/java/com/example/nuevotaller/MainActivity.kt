package com.example.nuevotaller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import com.example.nuevotaller.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var  binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private val articlesList = mutableListOf<Articles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchNews.setOnQueryTextListener(this)

        initRecyclerView()
        searchView("Business")
    }

    private fun initRecyclerView() {

        adapter = ArticleAdapter(articlesList)
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = adapter
    }

    private fun searchView(category: String)
    {
        val api = RetroFit2()

        CoroutineScope(Dispatchers.IO).launch {

            val call = api.getService()?.getNewByCategory("us", category, "4b94054dbc6b4b3b9e50d8f62cde4f6c")
            val news: NewResponse? = call?.body()

            runOnUiThread
            {
                if (call!!.isSuccessful){
                    if(news?.status.equals("ok")){


                    }else{
                        showMessage("Error en webservice")
                    }


                    val article = news?.articles?:emptyList()
                    articleList.clear()
                    articleList.addAll(article)
                    adapter.notifyDataSetChanged()
                }else
                {
                    showMessage("Error en el retrofit")
                }
                    hideKeyBoard()
                }
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showMessage() {
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty())
        {
            searchView(query.toLowerCase(Locale.ROOT))
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}