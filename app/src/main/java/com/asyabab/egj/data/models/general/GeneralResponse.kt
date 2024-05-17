package com.asyabab.egj.data.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GeneralResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null


    interface GeneralResponseCallback {
        fun onSuccess(generalResponse: GeneralResponse)
        fun onFailure(message: String)
    }
}