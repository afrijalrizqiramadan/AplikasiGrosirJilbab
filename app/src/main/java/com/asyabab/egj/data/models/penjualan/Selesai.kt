package com.asyabab.egj.data.models.penjualan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Selesai: Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("catatan")
    @Expose
    var catatan: String? = null
    @SerializedName("tanggal")
    @Expose
    var tanggal: String? = null
    @SerializedName("metodepembayaran")
    @Expose
    var metodepembayaran: String? = null

    @SerializedName("total_harga")
    @Expose
    var totalHarga: String? = null

    @SerializedName("user")
    @Expose
    var user: List<User__2>? = null
    @SerializedName("barang")
    @Expose
    var barang: List<Barang__2>? = null
}