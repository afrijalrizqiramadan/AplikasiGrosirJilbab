package com.asyabab.egj.data.models.barang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Datum: Serializable {
    @SerializedName("id_barang")
    @Expose
    var idBarang: String? = null

    @SerializedName("nama_barang")
    @Expose
    var namaBarang: String? = null

    @SerializedName("stok")
    @Expose
    var stok: String? = null

    @SerializedName("harga_stok")
    @Expose
    var hargaStok: String? = null

    @SerializedName("nama_gambar")
    @Expose
    var namaGambar: String? = null


}