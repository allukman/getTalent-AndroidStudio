package id.smartech.get_talent.activity.profile.detail_profile.company

import com.google.gson.annotations.SerializedName

data class DetailCompanyResponse (val success: Boolean, val message: String, val data: Data){
    data class Data(@SerializedName("com_id")val companyId: String,
                    @SerializedName("acc_id")val accountId: String,
                    @SerializedName("acc_nama")val accountName: String,
                    @SerializedName("acc_email")val companyEmail: String,
                    @SerializedName("acc_phone")val accountPhone: String,
                    @SerializedName("com_company")val companyName: String,
                    @SerializedName("com_position")val companyPosition: String,
                    @SerializedName("com_bidang")val companyBidang: String,
                    @SerializedName("com_city")val companyCity: String,
                    @SerializedName("com_description")val companyDescription: String,
                    @SerializedName("com_instagram")val companyInstagram: String,
                    @SerializedName("com_linkedin")val companyLinkedin: String,
                    @SerializedName("com_github")val companyGithub: String,
                    @SerializedName("com_photo")val companyPhoto: String)
}