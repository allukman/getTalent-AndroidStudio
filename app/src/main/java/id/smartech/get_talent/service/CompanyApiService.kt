package id.smartech.get_talent.service

import id.smartech.get_talent.activity.detailProfile.DetailCompanyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CompanyApiService {

    @GET("/company/{id}")
    suspend fun getCompanyById(@Path("id")comId: Int): DetailCompanyResponse

}