package id.smartech.get_talent.activity.main

import com.google.gson.annotations.SerializedName

data class GetEngineerIdResponse (val success : Boolean, val message : String, val data : Data) {
    data class Data(@SerializedName("en_id") val engineerId : String,
                    @SerializedName("acc_id") val accountId: String,
                    @SerializedName("acc_nama") val accountName: String,
                    @SerializedName("acc_email") val accountEmail: String
                          )
}