package com.asyabab.egj.data.models.kategori.listkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pivot : Serializable {
    @SerializedName("category_id")
    @Expose
    var categoryId: Int? = null

    @SerializedName("sub_category_id")
    @Expose
    var subCategoryId: Int? = null

}