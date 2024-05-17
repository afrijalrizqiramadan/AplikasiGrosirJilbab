package com.asyabab.egj.data.models.penjualan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PenjualanResponse {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    interface PenjualanResponseCallback {
        fun onSuccess(penjualanResponse: PenjualanResponse)
        fun onFailure(message: String)
    }
}