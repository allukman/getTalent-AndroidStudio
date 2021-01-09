package id.smartech.get_talent.activity.experience.createExperience

import android.util.Log
import androidx.lifecycle.ViewModel
import id.smartech.get_talent.activity.project.createProject.CreateResponse
import id.smartech.get_talent.service.ExperienceApiService
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class AddExperienceViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: ExperienceApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ExperienceApiService){
        this.service = service
    }

    fun createExperience(engineerId: String, companyName: String, jobPosition: String, startDate: String, endDate: String, xpDescription: String, photo: MultipartBody.Part){
        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
        val comName = companyName.toRequestBody("text/plain".toMediaTypeOrNull())
        val position = jobPosition.toRequestBody("text/plain".toMediaTypeOrNull())
        val start = startDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val end = endDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val xpDecs = xpDescription.toRequestBody("text/plain".toMediaTypeOrNull())

        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.createExperience(enId, comName, position, start, end, xpDecs, photo)
                } catch (e:Throwable){
                    e.printStackTrace()
                }
            }
            if (result is CreateResponse) {
                Log.d("responseSuccess", result.toString())
            }
        }
    }
}