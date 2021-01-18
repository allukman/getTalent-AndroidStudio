package id.smartech.get_talent.activity.hire.list_hire

import id.smartech.get_talent.activity.hire.response_hire.HireResponse
import id.smartech.get_talent.data.HireEngineerModel
import id.smartech.get_talent.service.HireService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GetHirePresenter(private val coroutineScope: CoroutineScope,
                        private val service: HireService): GetHireContract.Presenter {

    private var view: GetHireContract.View? = null

    override fun bindToView(view: GetHireContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }


    override fun getAllHireEngineer(enId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getHireEngineer(enId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("you don't have a hiring list")
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

            if (response is HireResponse) {
                view?.hideLoading()
                if (response.success) {
                    val list = response.data.map {
                        HireEngineerModel(
                            it.hireId,
                            it.engineerId,
                            it.projectId,
                            it.hirePrice,
                            it.projectName,
                            it.hireMessage,
                            it.hireStatus,
                            it.hireCreated,
                            it.projectDeadline
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