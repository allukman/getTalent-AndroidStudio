package id.smartech.get_talent.activity.profile.detailProfile

import com.google.gson.annotations.SerializedName

data class DetailEngineerResponse (val success : Boolean, val message : String, val data : Data) {
    data class Data(@SerializedName("en_id") val engineerId : String,
                    @SerializedName("acc_id") val accountId: String,
                    @SerializedName("acc_nama") val accountName: String,
                    @SerializedName("acc_email") val accountEmail: String,
                    @SerializedName("en_job_title") val engineerJobTitle: String,
                    @SerializedName("en_job_type") val engineerJobType: String,
                    @SerializedName("en_domisili") val engineerDomisili: String,
                    @SerializedName("en_instagram") val engineerInstagram: String,
                    @SerializedName("en_github") val engineerGithub: String,
                    @SerializedName("en_gitlab") val engineerGitlab: String,
                    @SerializedName("en_photo") val engineerPhoto: String,
                    @SerializedName("en_deskripsi") val engineerDeskripsi: String
    )
}