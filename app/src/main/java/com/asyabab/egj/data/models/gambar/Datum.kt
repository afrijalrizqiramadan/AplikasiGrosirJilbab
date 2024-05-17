package com.asyabab.egj.data.models.gambar

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Datum : Serializable {
    @SerializedName("id_gambar")
    @Expose
    var idGambar: String? = null

    @SerializedName("id_barang")
    @Expose
    var idBarang: String? = null

    @SerializedName("nama_gambar")
    @Expose
    var namaGambar: String? = null
}