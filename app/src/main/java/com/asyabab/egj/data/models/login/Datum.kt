package com.asyabab.egj.data.models.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Datum : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("nohp")
    @Expose
    var nohp: String? = null

    @SerializedName("alamat")
    @Expose
    var alamat: String? = null

    @SerializedName("gambar")
    @Expose
    var gambar: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
}