package com.asyabab.egj.data.models.item.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Category : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("discount")
    @Expose
    var discount: Int? = null

    @SerializedName("maximum")
    @Expose
    var maximum: Int? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("pivot")
    @Expose
    var pivot: Pivot? = null

}