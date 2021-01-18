package id.smartech.get_talent.activity.home

import id.smartech.get_talent.data.EngineerModel
import id.smartech.get_talent.service.EngineerApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomePresenter(private val coroutineScope: CoroutineScope,
                    private val service: EngineerApiService): HomeContract.Presenter {

    private var view: HomeContract.View? = null

    override fun bindToView(view: HomeContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getAllEngineer() {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getAllEngineer()
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("Data Engineer not found")
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
            if (response is EngineerResponse) {
                view?.hideLoading()
                if (response.success) {
                    val list = response.data?.map {
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
                    val mutable = list.toMutableList()
                    mutable.removeAll { it.engineerJobTitle == null || it.engineerJobType == null }
                    view?.onResultSuccess(mutable)
                } else {
                    view?.onResultFail(response.message)
                }
            }
        }
    }

}