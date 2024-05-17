package com.asyabab.egj.data.models.variasi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VariasiResponse {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

        interface VariasiResponseCallback {
        fun onSuccess(variasiResponse: VariasiResponse)
        fun onFailure(message: String)
    }
}