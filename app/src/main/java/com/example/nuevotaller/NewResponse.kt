package com.example.nuevotaller

import com.google.gson.annotations.SerializedName


data class NewResponse(
    @SerializedName("status") val statusString:String,
    @SerializedName("totalResults") val totalResults:String,
    @SerializedName("articles") val articles:List<Articles>

)

