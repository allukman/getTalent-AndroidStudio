package id.smartech.get_talent.activity.portofolio.detail_portofolio

import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.PortofolioApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailPortofolioPresenter(private val coroutineScope: CoroutineScope,
                                    private val service: PortofolioApiService): DetailPortofolioContract.Presenter {

    private var view: DetailPortofolioContract.View? = null

    override fun bindToView(view: DetailPortofolioContract.View) {
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

    override fun deletePortofolioByPrId(prId: String?) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.deletePortofolio(prId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessDeletePortofolio()
                }
            }
        }
    }
}