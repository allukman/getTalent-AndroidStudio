package id.smartech.get_talent.service

import id.smartech.get_talent.activity.hire.HireResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HireService {

    @GET("/hire/engineer/{id}")
    suspend fun getHireEngineer(@Path("id")engineerId: String?): HireResponse

}