package com.example.nuevotaller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
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
    //Variables para el país y la categoría a seleccionar.
    private var pais: String = ""
    private var categoria: String = ""

    //Variables para cada país a colocar en la app de noticias.
    lateinit var btnMexico: Button
    lateinit var btnArgentina: Button
    lateinit var btnJapon: Button
    lateinit var btnBelgica: Button
    lateinit var btnVenezuela: Button
    lateinit var btnItalia: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Vinculano las variables con sus ids del archivo .xml
        btnMexico = findViewById(R.id.btnMexico)
        btnArgentina = findViewById(R.id.btnArgentina)
        btnJapon = findViewById(R.id.btnJapon)
        btnBelgica = findViewById(R.id.btnBelgica)
        btnVenezuela = findViewById(R.id.btnVenezuela)
        btnItalia = findViewById(R.id.btnItalia)

        binding.searchNews.setOnQueryTextListener(this)

        initRecyclerView()

        //Buscando las noticias de México
        btnMexico.setOnClickListener(){
            searchView("mx","general")
        }

        //Buscando las noticias de Argentina
        btnArgentina.setOnClickListener(){
            searchView("ar","general")
        }

        //Buscando las noticias de Japón
        btnJapon.setOnClickListener(){
            searchView("jp","general")
        }

        //Buscando las noticias de Bélgica
        btnBelgica.setOnClickListener(){
            searchView("be","general")
        }

        //Buscando las noticias de Venezuela
        btnVenezuela.setOnClickListener(){
            searchView("ve","general")
        }

        //Buscando las noticias de Italia
        btnItalia.setOnClickListener(){
            searchView("it","general")
        }
    }

    private fun initRecyclerView() {

        adapter = ArticleAdapter(articlesList)
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = adapter
    }

    private fun searchView(country: String, category: String): String
    {
        val api = RetroFit2()

        pais = country
        categoria = category

        CoroutineScope(Dispatchers.IO).launch {

            val call = api.getService()?.getNewByCategory(country, category, "d598b7c8c5d14f5683d7a995500d8b3d")
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
        return country //Regresando al país.
    }


    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showMessage(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        var pais2: String = searchView(pais, categoria) //Inicializando las variables pais y categoria con el método searchView.

        if(!query.isNullOrEmpty())
        {
            searchView(pais2,query.toLowerCase(Locale.ROOT))

        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}