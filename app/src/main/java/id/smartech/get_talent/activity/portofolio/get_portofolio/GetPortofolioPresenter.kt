package id.smartech.get_talent.activity.portofolio.get_portofolio

import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByEngIdResponse
import id.smartech.get_talent.data.PortofolioModel
import id.smartech.get_talent.service.PortofolioApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GetPortofolioPresenter (private val coroutineScope: CoroutineScope,
                                private val service: PortofolioApiService) : GetPortofolioContract.Presenter {

    private var view: GetPortofolioContract.View? = null

    override fun bindToView(view: GetPortofolioContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getAllPortofolioEngineer(enId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getPortofolioByEngId(enId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()
                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("you don't have a Portofolio")
                            }
                            e.code() == 400 -> {
                                view?.onResultFail("Please re-login")
                            }
                            else -> {
                                view?.onResultFail("Server under maintenance")
                            }
                        }
                    }
                }
            }

            if(response is GetPortofolioByEngIdResponse) {
                view?.hideLoading()
                if (response.success) {
                    val list = response.data.map {
                        PortofolioModel(
                            it.portofolioId,
                            it.engineerId,
                            it.appName,
                            it.appDescription,
                            it.prLinkPub,
                            it.prLinkRepo,
                            it.workPlace,
                            it.appType,
                            it.portofolioPhoto
                        )
                    }
                    view?.onResultSuccess(list)
                } else {
                    view?.onResultFail(response.message)
                }

            }
        }
    }


}