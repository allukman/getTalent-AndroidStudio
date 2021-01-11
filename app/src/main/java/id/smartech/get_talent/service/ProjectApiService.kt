package id.smartech.get_talent.service


import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.activity.project.detailProject.ProjectIdResponse
import id.smartech.get_talent.activity.project.listProject.ProjectResponse
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
    suspend fun AddProject(
        @Part("comId")companyId: RequestBody,
        @Part("pjNamaProject")projectName: RequestBody,
        @Part("pjDeskripsi")projectDeskripsi: RequestBody,
        @Part("pjDeadline")projectDeadline: RequestBody,
        @Part photo: MultipartBody.Part

    ) : HelperResponse
}