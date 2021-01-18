package id.smartech.get_talent.activity.portofolio.update_portofolio

import android.widget.Toast
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import id.smartech.get_talent.activity.profile.detail_profile.engineer.DetailEngineerResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.PortofolioApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UpdatePortofolioPresenter (private val coroutineScope: CoroutineScope,
                                private val service: PortofolioApiService): UpdatePortofolioContract.Presenter {

    private var view: UpdatePortofolioContract.View? = null

    override fun bindToView(view: UpdatePortofolioContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getPortofolioByPrId(prId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getPortofolioByPrId(prId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is GetPortofolioByPrIdResponse) {
                view?.hideLoading()
                if (response.success) {
                    val data = response.data
                    view?.onResultSuccessGetPortofolio(data)
                }
            }
        }
    }

    override fun updatePortofolio(
        prId: String,
        engineerId: String,
        appName: String,
        appDesc: String,
        appPub: String,
        appRepo: String,
        appTpKerja: String,
        appType: String,
        photo: MultipartBody.Part
    ) {
        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
        val name = appName.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc = appDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        val pub = appPub.toRequestBody("text/plain".toMediaTypeOrNull())
        val repo = appRepo.toRequestBody("text/plain".toMediaTypeOrNull())
        val tpKerja = appTpKerja.toRequestBody("text/plain".toMediaTypeOrNull())
        val type = appType.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.updatePortofolio(prId, enId, name, desc, pub, repo, tpKerja, type, photo)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessUpdate("Success update portofolio")
                }
            }
        }
    }

    override fun updatePortofolioWithoutImage(
        prId: String,
        engineerId: String,
        appName: String,
        appDesc: String,
        appPub: String,
        appRepo: String,
        appTpKerja: String,
        appType: String
    ) {
        val enId = engineerId.toRequestBody("text/plain".toMediaTypeOrNull())
        val name = appName.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc = appDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        val pub = appPub.toRequestBody("text/plain".toMediaTypeOrNull())
        val repo = appRepo.toRequestBody("text/plain".toMediaTypeOrNull())
        val tpKerja = appTpKerja.toRequestBody("text/plain".toMediaTypeOrNull())
        val type = appType.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.updatePortofolioWithoutImage(prId, enId, name, desc, pub, repo, tpKerja, type)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessUpdate("Success update portofolio")
                }
            }
        }
    }

}