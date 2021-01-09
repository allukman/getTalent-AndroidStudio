package id.smartech.get_talent.service

import id.smartech.get_talent.activity.experience.GetExperienceByEngIdResponse
import id.smartech.get_talent.activity.project.createProject.CreateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ExperienceApiService {

    @GET("/experience/engineer/{id}")
    suspend fun getExperienceByEngId(@Path("id")engineerId: String?) : GetExperienceByEngIdResponse

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
    ) : CreateResponse
}