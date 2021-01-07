package id.smartech.get_talent.service

import id.smartech.get_talent.activity.hire.HireStatusResponse
import id.smartech.get_talent.activity.main.GetCompanyIdResponse
import id.smartech.get_talent.activity.project.ProjectIdResponse
import id.smartech.get_talent.activity.project.ProjectResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProjectApiService {

    @GET("/project/company/{id}")
    suspend fun getProjectByComId(@Path("id")companyId: Int ): ProjectResponse

    @GET("/project/{id}")
    suspend fun getProjectById(@Path("id")projectId: String?): ProjectIdResponse

    @Multipart
    @POST("/project")
    suspend fun createProject(@Part photo: MultipartBody.Part,
                              @Part("pjNamaProject") pjNamaProject: RequestBody,
                              @Part("pjDeskripsi") pjDeskripsi: RequestBody,
                              @Part("pjDeadline") pjDeadline: RequestBody) : HireStatusResponse
}