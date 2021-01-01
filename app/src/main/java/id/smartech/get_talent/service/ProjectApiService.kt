package id.smartech.get_talent.service

import id.smartech.get_talent.activity.project.ProjectResponse
import retrofit2.http.GET

interface ProjectApiService {

    @GET("/project/company/9")
    suspend fun getProjectByComId(): ProjectResponse
}