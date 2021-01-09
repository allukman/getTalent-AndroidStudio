package id.smartech.get_talent.service

import id.smartech.get_talent.activity.portofolio.GetPortofolioByEngIdResponse
import id.smartech.get_talent.activity.project.createProject.CreateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PortofolioApiService {

    @GET("/portofolio/engineer/{id}")
    suspend fun getPortofolioByEngId(@Path("id")engineerId: String?) : GetPortofolioByEngIdResponse

    @Multipart
    @POST("/portofolio")
    suspend fun createPortofolio(
        @Part("enId")engineerId: RequestBody,
        @Part("prAplikasi")appName: RequestBody,
        @Part("prDeskripsi")appDesc: RequestBody,
        @Part("prLinkPub")appLinkPub: RequestBody,
        @Part("prLinkRepo")appRepo: RequestBody,
        @Part("prTpKerja")workplace: RequestBody,
        @Part("prType")appType: RequestBody,
        @Part photo: MultipartBody.Part
    ) : CreateResponse
}