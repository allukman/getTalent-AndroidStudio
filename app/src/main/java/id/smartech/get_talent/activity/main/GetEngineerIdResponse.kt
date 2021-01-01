package id.smartech.get_talent.activity.main

import com.google.gson.annotations.SerializedName

class GetEngineerIdResponse (val success : String, val message : String, val data : List<>) {
    data class engineerId(@SerializedName("en_id") val engineerId : String,
                          @SerializedName("acc_id") val accountId: String,
                          @Ser
                          )
}