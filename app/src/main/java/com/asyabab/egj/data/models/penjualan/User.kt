package com.asyabab.egj.data.models.penjualan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class User : Serializable {
    @SerializedName("id_user")
    @Expose
    var idUser: String? = null

    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("alamat")
    @Expose
    var alamat: String? = null

    @SerializedName("nohp")
    @Expose
    var nohp: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("gambar")
    @Expose
    var gambar: String? = null
}