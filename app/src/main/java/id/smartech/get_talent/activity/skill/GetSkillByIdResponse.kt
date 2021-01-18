package id.smartech.get_talent.activity.skill

import com.google.gson.annotations.SerializedName

data class GetSkillByIdResponse (val success: Boolean, val message: String, val data: Data) {
    data class Data(@SerializedName("sk_id")val skillId: Int,
                     @SerializedName("en_id")val engineerId: Int,
                     @SerializedName("sk_nama_skill")val skillName: String?)
}