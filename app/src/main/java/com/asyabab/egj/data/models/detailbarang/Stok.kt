package com.asyabab.egj.data.models.detailbarang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Stok : Serializable {
    @SerializedName("id_stok")
    @Expose
    var idStok: String? = null

    @SerializedName("warna_stok")
    @Expose
    var warnaStok: String? = null

    @SerializedName("ukuran_stok")
    @Expose
    var ukuranStok: String? = null

    @SerializedName("jumlah_stok")
    @Expose
    var jumlahStok: String? = null

    @SerializedName("harga_stok")
    @Expose
    var hargaStok: String? = null
}