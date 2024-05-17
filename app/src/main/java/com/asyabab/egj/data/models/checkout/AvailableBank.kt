package com.asyabab.egj.data.models.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AvailableBank : Serializable {
    @SerializedName("bank_code")
    @Expose
    var bankCode: String? = null

    @SerializedName("collection_type")
    @Expose
    var collectionType: String? = null

    @SerializedName("bank_account_number")
    @Expose
    var bankAccountNumber: String? = null

    @SerializedName("transfer_amount")
    @Expose
    var transferAmount: Int? = null

    @SerializedName("bank_branch")
    @Expose
    var bankBranch: String? = null

    @SerializedName("account_holder_name")
    @Expose
    var accountHolderName: String? = null

    @SerializedName("identity_amount")
    @Expose
    var identityAmount: Int? = null

    companion object {
        private const val serialVersionUID = -2252443300237036757L
    }
}