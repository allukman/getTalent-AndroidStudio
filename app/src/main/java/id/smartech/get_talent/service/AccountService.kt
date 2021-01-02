package id.smartech.get_talent.service

import id.smartech.get_talent.activity.login.LoginResponse
import id.smartech.get_talent.activity.main.GetCompanyIdResponse
import id.smartech.get_talent.activity.main.GetEngineerIdResponse
import id.smartech.get_talent.activity.register.RegisterResponse
import id.smartech.get_talent.util.PrefHelper
import retrofit2.http.*

interface AccountService {

//    Login service
    @FormUrlEncoded
    @POST("/account/login")
    suspend fun loginRequest(@Field("accEmail") email: String,
                            @Field("accPassword") password: String) : LoginResponse

//    Register service

    @FormUrlEncoded
    @POST("/account/register")
    suspend fun registerEngineer(@Field("accName")name: String,
                                 @Field("accEmail")email: String,
                                 @Field("accPhone")phone: String,
                                 @Field("accPassword")password: String,
                                 @Field("accLevel")level: Int
                                        ) : RegisterResponse

    @FormUrlEncoded
    @POST("/account/register")
    suspend fun registerCompany(@Field("accName")name: String,
                                @Field("accEmail")email: String,
                                @Field("accPhone")phone: String,
                                @Field("accPassword")password: String,
                                @Field("accLevel")level: Int,
                                @Field("comCompany")companyName: String,
                                @Field("comPosition")position: String
                                         ) : RegisterResponse
    //    Get EngineerId

    @GET("/engineer/getId/{id}")
    suspend fun getEngineerIdByAccountId(@Path("id")enId: String? ): GetEngineerIdResponse

    //    Get CompanyId

    @GET("/company/getId/{id}")
    suspend fun getCompanyIdByAccountId(@Path("id")comId: String? ): GetCompanyIdResponse

}