package com.asyabab.egj.data.models.kategori.detailkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("categories")
    @Expose
    var categories: List<Category>? =
        null

    @SerializedName("offers")
    @Expose
    var offers: List<Offer>? =
        null

    @SerializedName("items")
    @Expose
    var items: List<Item>? =
        null

}