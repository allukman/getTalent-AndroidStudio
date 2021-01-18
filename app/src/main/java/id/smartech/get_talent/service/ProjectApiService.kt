package id.smartech.get_talent.service


import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.activity.project.response_project.ProjectIdResponse
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

    @Multipart
    @PUT("/project/{id}")
    suspend fun UpdateProject(
        @Path("id")projectId: String,
        @Part("comId")companyId: RequestBody,
        @Part("pjNamaProject")projectName: RequestBody,
        @Part("pjDeskripsi")projectDeskripsi: RequestBody,
        @Part("pjDeadline")projectDeadline: RequestBody,
        @Part photo: MultipartBody.Part
    ) : HelperResponse

    @Multipart
    @PUT("/project/{id}")
    suspend fun UpdateProjectWithoutImage(
        @Path("id")projectId: String,
        @Part("comId")companyId: RequestBody,
        @Part("pjNamaProject")projectName: RequestBody,
        @Part("pjDeskripsi")projectDeskripsi: RequestBody,
        @Part("pjDeadline")projectDeadline: RequestBody
    ) : HelperResponse
}