package com.asyabab.egj.data.models.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface RegisterResponseCallback {
        fun onSuccess(registerResponse: RegisterResponse)
        fun onFailure(message: String)
    }
}