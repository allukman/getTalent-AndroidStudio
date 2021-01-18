package id.smartech.get_talent.activity.experience.detail_experience

import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.ExperienceApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailExperiencePresenter(private val coroutineScope: CoroutineScope,
                                private val service: ExperienceApiService): DetailExperienceContract.Presenter  {

    private var view: DetailExperienceContract.View? = null

    override fun bindToView(view: DetailExperienceContract.View) {
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

    override fun deleteExperienceByXpId(xpId: String?) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.deleteExperience(xpId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessDeleteExperience()
                }
            }
        }
    }
}