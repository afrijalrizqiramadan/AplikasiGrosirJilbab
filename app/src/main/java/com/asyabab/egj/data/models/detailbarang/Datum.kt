package com.asyabab.egj.data.models.detailbarang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Datum : Serializable {
    @SerializedName("id_barang")
    @Expose
    var idBarang: String? = null
    @SerializedName("nama_barang")
    @Expose
    var namaBarang: String? = null

    @SerializedName("id_kategori")
    @Expose
    var idkategoriBarang: String? = null


    @SerializedName("nama_kategori")
    @Expose
    var kategoriBarang: String? = null

    @SerializedName("keterangan_barang")
    @Expose
    var keteranganBarang: String? = null

    @SerializedName("stokall")
    @Expose
    var stokall:  String? = null

    @SerializedName("harga_stok")
    @Expose
    var hargaStok: String? = null

    @SerializedName("gambar")
    @Expose
    var gambar: List<Gambar>? = null

    @SerializedName("stok")
    @Expose
    var stok: List<Stok>? = null
}