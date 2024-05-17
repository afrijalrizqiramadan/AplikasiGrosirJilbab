package com.asyabab.egj.data.models.detailbarang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailBarangResponse : Serializable {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    interface DetailBarangResponseCallback {
        fun onSuccess(detailBarangResponse: DetailBarangResponse)
        fun onFailure(message: String)
    }
}