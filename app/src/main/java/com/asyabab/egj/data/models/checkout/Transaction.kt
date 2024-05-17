package com.asyabab.egj.data.models.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Transaction : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("external_id")
    @Expose
    var externalId: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("merchant_name")
    @Expose
    var merchantName: String? = null

    @SerializedName("merchant_profile_picture_url")
    @Expose
    var merchantProfilePictureUrl: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = null

    @SerializedName("payer_email")
    @Expose
    var payerEmail: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("expiry_date")
    @Expose
    var expiryDate: String? = null

    @SerializedName("invoice_url")
    @Expose
    var invoiceUrl: String? = null

    @SerializedName("available_banks")
    @Expose
    var availableBanks: List<AvailableBank>? = null

    @SerializedName("available_ewallets")
    @Expose
    var availableEwallets: List<Any>? = null

    @SerializedName("should_exclude_credit_card")
    @Expose
    var shouldExcludeCreditCard: Boolean? = null

    @SerializedName("should_send_email")
    @Expose
    var shouldSendEmail: Boolean? = null

    @SerializedName("created")
    @Expose
    var created: String? = null

    @SerializedName("updated")
    @Expose
    var updated: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    companion object {
        private const val serialVersionUID = -6576690223064286651L
    }
}