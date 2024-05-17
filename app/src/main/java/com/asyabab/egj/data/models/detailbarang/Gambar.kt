package com.asyabab.egj.data.models.detailbarang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Gambar: Serializable {
    @SerializedName("id_gambar")
    @Expose
    var idGambar: String? = null

    @SerializedName("nama_gambar")
    @Expose
    var namaGambar: String? = null
}