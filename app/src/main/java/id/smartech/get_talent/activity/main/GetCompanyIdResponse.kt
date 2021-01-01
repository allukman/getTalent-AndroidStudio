package id.smartech.get_talent.activity.main

import com.google.gson.annotations.SerializedName

data class GetCompanyIdResponse (val success : Boolean, val message : String, val data : Data) {
    data class Data(@SerializedName("com_id") val comId : String?,
                    @SerializedName("acc_id") val accountId: String?,
                    @SerializedName("acc_nama") val accountName: String?,
                    @SerializedName("acc_email") val accountEmail: String?
    )
}