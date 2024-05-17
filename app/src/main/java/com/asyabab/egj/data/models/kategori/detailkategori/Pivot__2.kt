package com.asyabab.egj.data.models.kategori.detailkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pivot__2 : Serializable {
    @SerializedName("category_id")
    @Expose
    var categoryId: Int? = null

    @SerializedName("item_id")
    @Expose
    var itemId: Int? = null

}