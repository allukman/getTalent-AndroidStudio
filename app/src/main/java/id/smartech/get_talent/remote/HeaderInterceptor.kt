package id.smartech.get_talent.remote

import android.content.Context
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val context: Context) : Interceptor {
    private lateinit var prefHelper: PrefHelper

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        prefHelper = PrefHelper(context = context)
        val token = prefHelper.getString( Constant.TOKEN)
//       val tokenAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NfaWQiOjU5LCJhY2NfZW1haWwiOiJsdWttYW4zQGdtYWlsLmNvbSIsImFjY19sZXZlbCI6MCwiYWNjX3Bob25lIjoiMDgxNzI4MzcxMjg0IiwiaWF0IjoxNjA5MjUzMjEyLCJleHAiOjE2MDkyNTY4MTJ9.P_lIhzdf4ssDhwWLfWq2UNHDnz0IN3LVyURTz8kNYHg"
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        )
    }
}