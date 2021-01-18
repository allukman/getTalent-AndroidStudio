package id.smartech.get_talent.service

import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByEngIdResponse
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PortofolioApiService {

    @GET("/portofolio/engineer/{id}")
    suspend fun getPortofolioByEngId(@Path("id")engineerId: String?) : GetPortofolioByEngIdResponse

    @GET("/portofolio/{id}")
    suspend fun getPortofolioByPrId(@Path("id")portofolioId: String?) : GetPortofolioByPrIdResponse

    @DELETE("/portofolio/{id}")
    suspend fun deletePortofolio(@Path("id")portofolioId: String?) : HelperResponse

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
    ) : HelperResponse

    @Multipart
    @PUT("/portofolio/{id}")
    suspend fun updatePortofolio(
        @Path("id")portofolioId: String,
        @Part("enId")engineerId: RequestBody,
        @Part("prAplikasi")appName: RequestBody,
        @Part("prDeskripsi")appDesc: RequestBody,
        @Part("prLinkPub")appPub: RequestBody,
        @Part("prLinkRepo")appRepo: RequestBody,
        @Part("prTpKerja")appTpKerja: RequestBody,
        @Part("prType")appType: RequestBody,
        @Part photo: MultipartBody.Part
    ): HelperResponse

    @Multipart
    @PUT("/portofolio/{id}")
    suspend fun updatePortofolioWithoutImage(
        @Path("id")portofolioId: String,
        @Part("enId")engineerId: RequestBody,
        @Part("prAplikasi")appName: RequestBody,
        @Part("prDeskripsi")appDesc: RequestBody,
        @Part("prLinkPub")appPub: RequestBody,
        @Part("prLinkRepo")appRepo: RequestBody,
        @Part("prTpKerja")appTpKerja: RequestBody,
        @Part("prType")appType: RequestBody
    ): HelperResponse
}