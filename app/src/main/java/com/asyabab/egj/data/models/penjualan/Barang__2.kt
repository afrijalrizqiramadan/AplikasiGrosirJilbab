package com.asyabab.egj.data.models.penjualan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Barang__2 : Serializable {
    @SerializedName("id_stok")
    @Expose
    var idStok: String? = null
    @SerializedName("nama_barang")
    @Expose
    var namaBarang: String? = null

    @SerializedName("warna_stok")
    @Expose
    var warnaStok: String? = null

    @SerializedName("ukuran_stok")
    @Expose
    var ukuranStok: String? = null

    @SerializedName("harga_stok")
    @Expose
    var hargaStok: String? = null
    @SerializedName("jumlah_penjualan")
    @Expose
    var jumlahPenjualan: String? = null

    @SerializedName("subtotal_penjualan")
    @Expose
    var subtotalPenjualan: String? = null
}