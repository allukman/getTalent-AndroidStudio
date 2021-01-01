package id.smartech.get_talent.service

import id.smartech.get_talent.activity.project.ProjectResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProjectApiService {

    @GET("/project/company/{id}")
    suspend fun getProjectByComId(@Path("id")projectId: String?): ProjectResponse
}