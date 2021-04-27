package com.example.nuevotaller

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFit2 {


        private var urls: APIService? = null

        init {

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            urls = retrofit.create(APIService::class.java)
        }

        fun getService(): APIService?{
            return urls
        }
}