package id.smartech.get_talent.activity.experience

import com.google.gson.annotations.SerializedName

data class GetExperienceByXpIdResponse(val success : Boolean, val message : String, val data : Data) {
    data class Data(@SerializedName("ex_id")val experienceId: String,
                    @SerializedName("en_id")val engineerId: String,
                    @SerializedName("ex_posisi")val position: String,
                    @SerializedName("ex_company")val exCompany: String,
                    @SerializedName("ex_start")val exStart: String,
                    @SerializedName("ex_end")val exEnd: String,
                    @SerializedName("ex_deskripsi")val exDesc: String,
                    @SerializedName("ex_photo")val photo: String
    )
}