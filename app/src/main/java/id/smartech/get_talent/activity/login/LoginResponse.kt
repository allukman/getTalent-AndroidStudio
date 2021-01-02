package id.smartech.get_talent.activity.login

import com.google.gson.annotations.SerializedName

data class LoginResponse (val success: Boolean, val message: String, val data: Data ) {
    data class Data(
        @SerializedName("acc_id") val accountId: String,
        @SerializedName("acc_nama") val accountName: String,
        @SerializedName("acc_email") val accountEmail: String,
        @SerializedName("acc_level") val accountLevel: String,
        val token: String
    )
}