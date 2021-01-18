package id.smartech.get_talent.service

import id.smartech.get_talent.activity.profile.detail_profile.engineer.DetailEngineerResponse
import id.smartech.get_talent.activity.home.EngineerResponse
import id.smartech.get_talent.activity.response.HelperResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface EngineerApiService {

    @GET("/engineer")
    suspend fun getAllEngineer(): EngineerResponse

    @GET("/engineer/{id}")
    suspend fun getEngineerByEngId(@Path("id")enId:String?) : DetailEngineerResponse

    @Multipart
    @PUT("/engineer/{id}")
    suspend fun updateEngineer(
        @Path("id")engineerId: String?,
        @Part("enJobTitle")jobTitle: RequestBody,
        @Part("enJobType")jobType: RequestBody,
        @Part("enDomisili")domisili: RequestBody,
        @Part("enDeskripsi")description: RequestBody,
        @Part("enInstagram")instagram: RequestBody,
        @Part("enGithub")github: RequestBody,
        @Part("enGitlab")gitlab: RequestBody,
        @Part photo: MultipartBody.Part
    ): HelperResponse

    @Multipart
    @PUT("/engineer/{id}")
    suspend fun updateEngineerWithoutImage(
        @Path("id")engineerId: String?,
        @Part("enJobTitle")jobTitle: RequestBody,
        @Part("enJobType")jobType: RequestBody,
        @Part("enDomisili")domisili: RequestBody,
        @Part("enDeskripsi")description: RequestBody,
        @Part("enInstagram")instagram: RequestBody,
        @Part("enGithub")github: RequestBody,
        @Part("enGitlab")gitlab: RequestBody
    ): HelperResponse

    @GET("/engineer/filter")
    suspend fun getAllEngineerSearch(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("filter") filter: Int? = null
    ) : EngineerResponse
}