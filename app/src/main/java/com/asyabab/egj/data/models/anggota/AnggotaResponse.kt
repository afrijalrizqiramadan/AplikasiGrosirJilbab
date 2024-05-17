package com.asyabab.egj.data.models.anggota

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AnggotaResponse : Serializable {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    interface AnggotaResponseCallback {
        fun onSuccess(anggotaResponse: AnggotaResponse)
        fun onFailure(message: String)
    }

}