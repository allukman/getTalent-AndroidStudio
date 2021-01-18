package id.smartech.get_talent.activity.hire.response_hire

import com.google.gson.annotations.SerializedName

data class GetHireByIdResponse(val success : Boolean, val message : String, val data : Data) {
    data class Data(@SerializedName("hr_id") val hireId : String?,
                    @SerializedName("en_id") val engineerId : String?,
                    @SerializedName("pj_id") val projectId : String?,
                    @SerializedName("hr_price") val hirePrice : String?,
                    @SerializedName("hr_message") val hireMessage : String?,
                    @SerializedName("hr_status") val hireStatus : String?,
                    @SerializedName("hr_createAt") val hireCreated : String?,
                    @SerializedName("pj_deadline") val projectDeadline : String?
    )
}