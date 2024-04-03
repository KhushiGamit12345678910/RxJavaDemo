package com.example.rxjavademo.models

import com.google.gson.annotations.SerializedName


class DataModel(
    @SerializedName("id") var id : Int = 0,
    @SerializedName("title") var title : String = "",
    @SerializedName("body") var body : String = ""

): java.io.Serializable