package com.asyabab.egj.data.models.item.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Image : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("item_id")
    @Expose
    var itemId: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

}