package id.smartech.get_talent.activity.portofolio

import com.google.gson.annotations.SerializedName

data class GetPortofolioByEngIdResponse (val success: Boolean, val message: String, val data: List<Portofolio>){
    data class Portofolio(@SerializedName("pr_id")val portofolioId: String,
                            @SerializedName("en_id")val engineerId: String,
                            @SerializedName("pr_aplikasi")val appName: String,
                            @SerializedName("pr_deskripsi")val appDescription: String,
                            @SerializedName("pr_link_pub") val prLinkPub: String,
                            @SerializedName("pr_link_repo")val prLinkRepo: String,
                            @SerializedName("pr_tp_kerja")val workPlace: String,
                            @SerializedName("pr_type")val appType: String,
                            @SerializedName("pr_photo")val portofolioPhoto: String)
}