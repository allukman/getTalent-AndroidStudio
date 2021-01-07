package id.smartech.get_talent.service

import id.smartech.get_talent.activity.experience.GetExperienceByEngIdResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExperienceApiService {

    @GET("/experience/engineer/{id}")
    suspend fun getExperienceByEngId(@Path("id")engineerId: String?) : GetExperienceByEngIdResponse
}