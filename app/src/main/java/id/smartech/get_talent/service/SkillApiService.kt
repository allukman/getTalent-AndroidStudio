package id.smartech.get_talent.service

import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.activity.skill.GetSkillByIdResponse
import id.smartech.get_talent.activity.skill.GetSkillResponse
import retrofit2.http.*

interface SkillApiService {

    @GET("/skill/engineer/{id}")
    suspend fun getSkill(@Path("id")enId:String?): GetSkillResponse

    @FormUrlEncoded
    @POST("/skill")
    suspend fun createSkill(@Field("en_id")engineerId: String?,
                            @Field("sk_nama_skill")skillName: String) : HelperResponse

    @GET("/skill/{id}")
    suspend fun getSkillById(@Path("id")skillId: Int): GetSkillByIdResponse

    @FormUrlEncoded
    @PUT("/skill/{id}")
    suspend fun updateSkill(@Path("id")skillId: Int,
                            @Field("sk_nama_skill")skillname: String?) : HelperResponse

    @DELETE("/skill/{id}")
    suspend fun deleteSkill(@Path("id")skillId: Int): HelperResponse
}