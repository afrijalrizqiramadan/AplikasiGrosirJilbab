package com.asyabab.egj.data.models.item.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("search")
    @Expose
    var search: String? = null

    @SerializedName("order")
    @Expose
    var order: Any? = null

    @SerializedName("filter_price_min")
    @Expose
    var filterPriceMin: Any? = null

    @SerializedName("filter_price_max")
    @Expose
    var filterPriceMax: Any? = null

    @SerializedName("results")
    @Expose
    var results: List<Result>? =
        null

}