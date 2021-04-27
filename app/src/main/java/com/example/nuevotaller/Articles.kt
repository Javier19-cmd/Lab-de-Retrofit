package com.example.nuevotaller

import com.google.gson.annotations.SerializedName

data class Articles(
    @SerializedName("autor") val author:String,
    @SerializedName("totalResults") val title:String,
    @SerializedName("description") val description:String,
    @SerializedName("urlToImage") val urlToImage:String
)