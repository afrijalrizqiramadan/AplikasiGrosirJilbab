package com.asyabab.egj.data.models.item.detailitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Image : Serializable {
    @SerializedName("id_gambar")
    @Expose
    var id: Int? = null

    @SerializedName("nama_gambar")
    @Expose
    var name: String? = null

}