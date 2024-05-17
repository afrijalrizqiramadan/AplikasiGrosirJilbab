package com.asyabab.egj.data.models.kategori.detailkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailKategoriResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    interface DetailKategoriResponseCallback {
        fun onSuccess(detailKategoriResponse: DetailKategoriResponse)
        fun onFailure(message: String)
    }
}