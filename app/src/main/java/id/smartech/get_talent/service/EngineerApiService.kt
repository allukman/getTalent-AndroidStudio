package id.smartech.get_talent.service

import id.smartech.get_talent.activity.home.EngineerResponse
import retrofit2.http.GET

interface EngineerApiService {

    @GET("/engineer/filter?filter=5")
    suspend fun getEngineerWebDeveloper(): EngineerResponse

    @GET("/engineer/filter?filter=6")
    suspend fun getEngineerAndroidDeveloper(): EngineerResponse

}