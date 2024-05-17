package com.asyabab.egj.data.models.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("location_id")
    @Expose
    var locationId: String? = null

    @SerializedName("shipmentable_type")
    @Expose
    var shipmentableType: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("total_shipment")
    @Expose
    var totalShipment: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("type_uid")
    @Expose
    var typeUid: String? = null

    @SerializedName("expire_date")
    @Expose
    var expireDate: String? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("shipmentable_id")
    @Expose
    var shipmentableId: String? = null

    @SerializedName("transaction")
    @Expose
    var transaction: Transaction? = null

    companion object {
        private const val serialVersionUID = 6744798625038104053L
    }
}