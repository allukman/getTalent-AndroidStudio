package id.smartech.get_talent.service

import id.smartech.get_talent.activity.hire.response_hire.CreateHireResponse
import id.smartech.get_talent.activity.hire.response_hire.HireResponse
import id.smartech.get_talent.activity.hire.response_hire.HireStatusResponse
import id.smartech.get_talent.activity.hire.response_hire.GetHireByIdResponse
import retrofit2.http.*

interface HireService {

    @GET("/hire/engineer/{id}")
    suspend fun getHireEngineer(@Path("id")engineerId: String?): HireResponse

    @FormUrlEncoded
    @POST("hire")
    suspend fun createHire(@Field("en_id")engineerId: String?,
                           @Field("pj_id")projectId: String?,
                           @Field("hr_price")hirePrice: String?,
                           @Field("hr_message")hireMessage: String?
                        ) : CreateHireResponse

    @FormUrlEncoded
    @PUT("hire/{id}")
    suspend fun updateStatus(@Path("id")hireId: String?,
                            @Field("hr_status")hireStatus: String?): HireStatusResponse

    @GET("/hire/{id}")
    suspend fun getHireById(@Path("id")hireId: String?): GetHireByIdResponse
}