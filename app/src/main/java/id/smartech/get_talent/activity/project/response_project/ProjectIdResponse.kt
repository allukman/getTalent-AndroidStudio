package id.smartech.get_talent.activity.project.response_project

import com.google.gson.annotations.SerializedName

data class ProjectIdResponse(val success: Boolean, val message: String, val data: Data){
    data class Data(@SerializedName("pj_id") val projectId : Int,
                    @SerializedName("com_id") val companyId : String?,
                    @SerializedName("pj_nama_project") val projectName : String?,
                    @SerializedName("pj_deskripsi") val projectDesc : String?,
                    @SerializedName("pj_deadline") val projectDeadline : String?,
                    @SerializedName("pj_gambar") val projectImage : String?,
                    @SerializedName("pj_createAt") val projectCreateAt : String?,
                    @SerializedName("pj_updateAt") val projectUpdateAt : String?)
}