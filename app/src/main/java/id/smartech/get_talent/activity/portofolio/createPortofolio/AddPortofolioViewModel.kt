package id.smartech.get_talent.activity.portofolio.createPortofolio

import android.util.Log
import androidx.lifecycle.ViewModel
import id.smartech.get_talent.activity.project.createProject.CreateResponse
import id.smartech.get_talent.service.ExperienceApiService
import id.smartech.get_talent.service.PortofolioApiService
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class AddPortofolioViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: PortofolioApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: PortofolioApiService) {
        this.service = service
    }

    fun createPortofolio(engineerId: String, applicationName: String, appDescription: String, appLinkPub: String, repository: String, workplace: String, appType: String, photo: MultipartBody.Part) {
        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
        val appName = applicationName.toRequestBody("text/plain".toMediaTypeOrNull())
        val appDesc = appDescription.toRequestBody("text/plain".toMediaTypeOrNull())
        val pub = appLinkPub.toRequestBody("text/plain".toMediaTypeOrNull())
        val repo = repository.toRequestBody("text/plain".toMediaTypeOrNull())
        val tpKerja = workplace.toRequestBody("text/plain".toMediaTypeOrNull())
        val type = appType.toRequestBody("text/plain".toMediaTypeOrNull())

        launch {
            Log.d("android2", "Start: ${Thread.currentThread().name}")
            val result = withContext(Dispatchers.IO) {
                Log.d("android2", "CallApi: ${Thread.currentThread().name}")
                try {
                    service?.createPortofolio(enId, appName, appDesc, pub, repo, tpKerja, type, photo)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is CreateResponse) {
                Log.d("responseSuccess", result.toString())
            }
        }

    }


}