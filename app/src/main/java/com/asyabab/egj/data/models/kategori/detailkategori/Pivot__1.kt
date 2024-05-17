package com.asyabab.egj.data.models.kategori.detailkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pivot__1 : Serializable {
    @SerializedName("offerable_id")
    @Expose
    var offerableId: Int? = null

    @SerializedName("offer_id")
    @Expose
    var offerId: Int? = null

    @SerializedName("offerable_type")
    @Expose
    var offerableType: String? = null

}