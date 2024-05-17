package com.asyabab.egj.data.models.keranjang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Datum: Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("idbarang")
    @Expose
    var idbarang: String? = null
    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("warna")
    @Expose
    var warna: String? = null

    @SerializedName("ukuran")
    @Expose
    var ukuran: String? = null

    @SerializedName("harga")
    @Expose
    var harga: String? = null

    @SerializedName("jumlah")
    @Expose
    var jumlah: String? = null

    @SerializedName("inventory")
    @Expose
    var inventory: String? = null

    @SerializedName("gambar")
    @Expose
    var gambar: String? = null
}