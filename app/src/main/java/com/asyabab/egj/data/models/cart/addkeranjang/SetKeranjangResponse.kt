package com.asyabab.egj.data.models.cart.addkeranjang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SetKeranjangResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface SetKeranjangResponseCallback {
        fun onSuccess(setKeranjangResponse: SetKeranjangResponse)
        fun onFailure(message: String)
    }
}