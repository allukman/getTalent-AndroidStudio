package id.smartech.get_talent.service

import id.smartech.get_talent.activity.profile.detail_profile.company.DetailCompanyResponse
import id.smartech.get_talent.activity.response.HelperResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CompanyApiService {

    @GET("/company/{id}")
    suspend fun getCompanyById(@Path("id")comId: Int): DetailCompanyResponse

    @Multipart
    @PUT("/company/{id}")
    suspend fun updateCompany(
        @Path("id")engineerId: Int,
        @Part("comCompany")comName: RequestBody,
        @Part("comPosition")position: RequestBody,
        @Part("comBidang")bidang: RequestBody,
        @Part("comCity")location: RequestBody,
        @Part("comDescription")desc: RequestBody,
        @Part("comInstagram")instagram: RequestBody,
        @Part("comLinkedin")linkedin: RequestBody,
        @Part("comGithub")github: RequestBody,
        @Part photo: MultipartBody.Part
    ): HelperResponse

    @Multipart
    @PUT("/company/{id}")
    suspend fun updateCompanyWithoutImage(
        @Path("id")engineerId: Int,
        @Part("comCompany")comName: RequestBody,
        @Part("comPosition")position: RequestBody,
        @Part("comBidang")bidang: RequestBody,
        @Part("comCity")location: RequestBody,
        @Part("comDescription")desc: RequestBody,
        @Part("comInstagram")instagram: RequestBody,
        @Part("comLinkedin")linkedin: RequestBody,
        @Part("comGithub")github: RequestBody
    ): HelperResponse

}