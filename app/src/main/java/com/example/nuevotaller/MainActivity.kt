package com.example.nuevotaller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.nuevotaller.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var  binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private val articlesList = mutableListOf<Articles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchNews.setOnQueryTextListener(this)

        initRecyclerView()
        searchView("general")
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

            val call = api.getService()?.getNewByCategory("us", category, "d598b7c8c5d14f5683d7a995500d8b3d")
            val news: NewResponse? = call?.body()

            runOnUiThread{
                if(call!!.isSuccessful){
                    if (news?.status.equals("ok")){
                        val articles = news?.articles ?: emptyList()
                        articlesList.clear()
                        articlesList.addAll(articles)
                        adapter.notifyDataSetChanged()
                    }else{
                        showMessage("Error en webservice")
                    }
                }else{
                    showMessage("Error en retrofit")
                }
            }
        }

    }


    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showMessage(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
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