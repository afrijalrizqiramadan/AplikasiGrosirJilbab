package com.asyabab.egj.data.models.kategori.listkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Datum : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("nama")
    @Expose
    var name: String? = null
}