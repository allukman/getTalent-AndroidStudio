package id.smartech.get_talent.service

import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByEngIdResponse
import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ExperienceApiService {

    @GET("/experience/engineer/{id}")
    suspend fun getExperienceByEngId(@Path("id")engineerId: String?) : GetExperienceByEngIdResponse

    @GET("experience/{id}")
    suspend fun getExperienceByXpId(@Path("id")experienceId: String?) : GetExperienceByXpIdResponse

    @DELETE("experience/{id}")
    suspend fun deleteExperience(@Path("id")experienceId: String?): HelperResponse

    @Multipart
    @POST("/experience")
    suspend fun createExperience(
        @Part("enId")engineerId: RequestBody,
        @Part("exPosisi")xpPosisi: RequestBody,
        @Part("exCompany")xpCompany: RequestBody,
        @Part("exStart")xpStartDate: RequestBody,
        @Part("exEnd")xpEndDate: RequestBody,
        @Part("exDeskripsi")xpDescription: RequestBody,
        @Part photo: MultipartBody.Part
    ) : HelperResponse

    @Multipart
    @PUT("/experience/{id}")
    suspend fun updateExperience(
        @Path("id")experienceId: String,
        @Part("enId")engineerId: RequestBody,
        @Part("exPosisi")position: RequestBody,
        @Part("exCompany")companyName: RequestBody,
        @Part("exStart")startDate: RequestBody,
        @Part("exEnd")endDate: RequestBody,
        @Part("exDeskripsi")desc: RequestBody,
        @Part photo: MultipartBody.Part
    ) : HelperResponse

    @Multipart
    @PUT("/experience/{id}")
    suspend fun updateExperienceWithoutImage(
        @Path("id")experienceId: String,
        @Part("enId")engineerId: RequestBody,
        @Part("exPosisi")position: RequestBody,
        @Part("exCompany")companyName: RequestBody,
        @Part("exStart")startDate: RequestBody,
        @Part("exEnd")endDate: RequestBody,
        @Part("exDeskripsi")desc: RequestBody
    ) : HelperResponse
}