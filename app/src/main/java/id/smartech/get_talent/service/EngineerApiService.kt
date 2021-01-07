package id.smartech.get_talent.service

import id.smartech.get_talent.activity.detailProfile.DetailEngineerResponse
import id.smartech.get_talent.activity.home.EngineerResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface EngineerApiService {

    @GET("/engineer")
    suspend fun getEngineerWebDeveloper(): EngineerResponse

    @GET("/engineer/{id}")
    suspend fun getEngineerByEngId(@Path("id")enId:String?) : DetailEngineerResponse
}