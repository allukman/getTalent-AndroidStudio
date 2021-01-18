package id.smartech.get_talent.activity.skill

import com.google.gson.annotations.SerializedName

data class GetSkillResponse (val success: Boolean, val message: String, val data: List<Skill>) {
    data class Skill(@SerializedName("sk_id")val skillId: Int,
                     @SerializedName("en_id")val engineerId: Int,
                     @SerializedName("sk_nama_skill")val skillName: String?)
}