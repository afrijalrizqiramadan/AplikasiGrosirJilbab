package com.asyabab.egj.data.models.keranjang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class KeranjangResponse : Serializable {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
    interface KeranjangResponseCallback {
        fun onSuccess(keranjangResponse: KeranjangResponse)
        fun onFailure(message: String)
    }
}