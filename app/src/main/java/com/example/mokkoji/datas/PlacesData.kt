package com.example.mokkoji.datas

import com.google.gson.annotations.SerializedName

data class PlacesData(
    val Id : Int,
    @SerializedName("name")
    val title : String,
) {
}