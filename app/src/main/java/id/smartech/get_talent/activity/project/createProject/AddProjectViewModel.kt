package id.smartech.get_talent.activity.project.createProject

import android.util.Log
import androidx.lifecycle.ViewModel
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.ProjectApiService
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class AddProjectViewModel : ViewModel(), CoroutineScope {

    private lateinit var service: ProjectApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ProjectApiService) {
        this.service = service
    }

    fun createProject(companyId: String, projectName: String, projectDeskripsi: String, projectDeadline: String, photo: MultipartBody.Part) {
        val comId = companyId.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjName = projectName.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjDeskripsi = projectDeskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
        val pjDeadline = projectDeadline.toRequestBody("text/plain".toMediaTypeOrNull())


        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.AddProject(comId, pjName, pjDeskripsi, pjDeadline, photo)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is HelperResponse) {
                Log.d("responseSuccess", result.toString())
            }
        }
    }
}