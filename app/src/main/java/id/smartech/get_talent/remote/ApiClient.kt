package id.smartech.get_talent.remote

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private const val BASE_URL = "http://174.129.47.146:4000/"
        private var retrofit : Retrofit? = null

        private fun providenHttpLoggingInterceptor() = run {
            HttpLoggingInterceptor().apply {
                apply {
                    level = HttpLoggingInterceptor.Level.BODY }
            }
        }


        fun getApiClient(context: Context) : Retrofit? {

            if(retrofit == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(providenHttpLoggingInterceptor())
                    .addInterceptor(HeaderInterceptor(context))
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client((okHttpClient))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit

        }

    }
}