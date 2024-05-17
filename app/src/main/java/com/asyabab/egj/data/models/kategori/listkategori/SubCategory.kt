package com.asyabab.egj.data.models.kategori.listkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubCategory : Serializable {
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
    var discount: Any? = null

    @SerializedName("maximum")
    @Expose
    var maximum: Any? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("pivot")
    @Expose
    var pivot: Pivot? = null

    @SerializedName("sub_categories")
    @Expose
    var subCategories: List<SubCategory_>? = null

}