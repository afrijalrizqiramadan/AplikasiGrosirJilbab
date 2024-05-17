package com.asyabab.egj.data.models.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("token_fcm")
    @Expose
    var tokenFcm: String? = null

    @SerializedName("device_id")
    @Expose
    var deviceId: String? = null

    @SerializedName("is_active")
    @Expose
    var isActive: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

}