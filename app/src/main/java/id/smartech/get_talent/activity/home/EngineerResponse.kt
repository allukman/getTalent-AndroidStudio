package id.smartech.get_talent.activity.home

import com.google.gson.annotations.SerializedName

data class EngineerResponse(val success : String, val message : String, val data : List<Home>) {
    data class Home(@SerializedName("en_id") val engineerId : String,
                       @SerializedName("ac_id") val accountId : String,
                       @SerializedName("ac_name") val accountName : String,
                       @SerializedName("en_job_title") val engineerJobTitle : String?,
                       @SerializedName("en_job_type") val engineerJobType : String?,
                       @SerializedName("en_domicile") val engineerDomicile : String?,
                        @SerializedName("en_profile") val engineerPhoto: String?
    )
}