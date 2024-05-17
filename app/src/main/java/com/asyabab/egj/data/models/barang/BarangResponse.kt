package com.asyabab.egj.data.models.barang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BarangResponse: Serializable {
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    interface BarangResponseCallback {
        fun onSuccess(barangResponse: BarangResponse)
        fun onFailure(message: String)
    }

}