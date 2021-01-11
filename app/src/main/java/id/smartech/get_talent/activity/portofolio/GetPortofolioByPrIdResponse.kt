package id.smartech.get_talent.activity.portofolio

import com.google.gson.annotations.SerializedName

data class GetPortofolioByPrIdResponse(val success : Boolean, val message : String, val data : Data) {
    data class Data(@SerializedName("pr_id")val portofolioId: String,
                    @SerializedName("en_id")val engineerId: String,
                    @SerializedName("pr_aplikasi")val appName: String,
                    @SerializedName("pr_deskripsi")val appDesc: String,
                    @SerializedName("pr_link_pub")val linkPub: String,
                    @SerializedName("pr_link_repo")val repository: String,
                    @SerializedName("pr_type")val appType: String,
                    @SerializedName("pr_photo")val portofolioPhoto: String
    )
}