package com.asyabab.egj.data.models.kategori.listkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ListKategoriResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? =
        null

    interface ListKategoriResponseCallback {
        fun onSuccess(listKategoriResponse: ListKategoriResponse)
        fun onFailure(message: String)
    }
}