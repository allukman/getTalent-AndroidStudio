package id.smartech.get_talent.activity.experience

import com.google.gson.annotations.SerializedName

data class GetExperienceByEngIdResponse (val success: Boolean, val message: String, val data: List<Experience>){
    data class Experience(@SerializedName("ex_id")val experienceId: String,
                          @SerializedName("en_id")val engineerId: String,
                          @SerializedName("ex_posisi")val experiencePosisi: String,
                          @SerializedName("ex_company")val experienceCompany: String,
                          @SerializedName("ex_start")val experienceStart: String,
                          @SerializedName("ex_end")val experienceEnd: String,
                          @SerializedName("ex_deskripsi")val experienceDeskripsi: String,
                          @SerializedName("ex_photo")val experiencePhoto: String)
}