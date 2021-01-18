package id.smartech.get_talent.activity.experience.update_experience

import android.widget.Toast
import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.ExperienceApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateExperiencePresenter(private val coroutineScope: CoroutineScope,
                                private val service: ExperienceApiService): UpdateExperienceContract.Presenter {

    private var view: UpdateExperienceContract.View? = null

    override fun bindToView(view: UpdateExperienceContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getExperienceByXpId(xpId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getExperienceByXpId(xpId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is GetExperienceByXpIdResponse) {
                view?.hideLoading()
                if (response.success) {
                    val data = response.data
                    view?.onResultSuccessGetExperience(data)
                }
            }
        }
    }

    override fun updateExperience(
        xpId: String,
        engineerId: String,
        jobPosition: String,
        companyName: String,
        exStart: String,
        exEnd: String,
        exDesc: String,
        photo: MultipartBody.Part
    ) {
        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
        val position = jobPosition.toRequestBody("text/plain".toMediaTypeOrNull())
        val comName = companyName.toRequestBody("text/plain".toMediaTypeOrNull())
        val start = exStart.toRequestBody("text/plain".toMediaTypeOrNull())
        val end = exEnd.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc = exDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.updateExperience(xpId, enId, position, comName, start, end, desc, photo)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessUpdate("Success update Experience")
                }
            }
        }
    }

    override fun updateExperienceWithoutImage(
        xpId: String,
        engineerId: String,
        jobPosition: String,
        companyName: String,
        exStart: String,
        exEnd: String,
        exDesc: String
    ) {
        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
        val position = jobPosition.toRequestBody("text/plain".toMediaTypeOrNull())
        val comName = companyName.toRequestBody("text/plain".toMediaTypeOrNull())
        val start = exStart.toRequestBody("text/plain".toMediaTypeOrNull())
        val end = exEnd.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc = exDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.updateExperienceWithoutImage(xpId, enId, position, comName, start, end, desc)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessUpdate("Success update Experience")
                }
            }
        }
    }
}