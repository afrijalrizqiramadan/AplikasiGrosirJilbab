package com.asyabab.egj.data.models.gambar

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GambarResponse : Serializable {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    interface GambarResponseCallback {
        fun onSuccess(gambarResponse: GambarResponse)
        fun onFailure(message: String)
    }
}