package id.smartech.get_talent.activity.search

import id.smartech.get_talent.activity.home.EngineerResponse
import id.smartech.get_talent.data.EngineerModel
import id.smartech.get_talent.service.EngineerApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SearchPresenter(private val coroutineScope: CoroutineScope,
                      private val service: EngineerApiService): SearchConstract.Presenter {

    private var view: SearchConstract.View? = null

    override fun bindToView(view: SearchConstract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun searchEngineer(search: String?, filter: Int?) {
        coroutineScope.launch {
            view?.showLoading()
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllEngineerSearch(search = search, filter = filter)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFailed("Engineer Not Found")
                            }
                            e.code() == 400 -> {
                                view?.onResultFailed("Please re-login")
                            }
                            else -> {
                                view?.onResultFailed("Server Under Maintenance")
                            }
                        }
                    }

                }
            }

            if (result is EngineerResponse) {
                view?.hideLoading()
                if (result.success) {
                    val list = result.data.map {
                        EngineerModel(
                            it.engineerId,
                            it.accountId,
                            it.accountName,
                            it.engineerJobTitle,
                            it.engineerJobType,
                            it.engineerDomicile,
                            it.engineerPhoto
                            )
                    }
                    view?.onResultSuccess(list)
                } else {
                    view?.onResultFailed(result.message)
                }
            }
        }
    }


}