package com.asyabab.egj.data.models.variasi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Datum : Serializable {
    @SerializedName("ukuran")
    @Expose
    var ukuran: List<String>? = null

    @SerializedName("warna")
    @Expose
    var warna: List<String>? = null
}