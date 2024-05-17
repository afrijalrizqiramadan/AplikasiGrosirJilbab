package com.asyabab.egj.data.models.item.detailitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailItemResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface DetailItemResponseCallback {
        fun onSuccess(detailItemResponse: DetailItemResponse)
        fun onFailure(message: String)
    }
}