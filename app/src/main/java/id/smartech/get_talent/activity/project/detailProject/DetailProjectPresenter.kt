package id.smartech.get_talent.activity.project.detailProject

import id.smartech.get_talent.activity.hire.response_hire.GetHireByIdResponse
import id.smartech.get_talent.activity.project.response_project.ProjectIdResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.HireService
import id.smartech.get_talent.service.ProjectApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailProjectPresenter (private val coroutineScope: CoroutineScope,
                              private val serviceProject: ProjectApiService,
                              private val serviceHire: HireService): DetailProjectContract.Presenter {

    private var view: DetailProjectContract.View? = null

    override fun bindToView(view: DetailProjectContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getProjectByPjId(pjId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceProject.getProjectById(pjId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is ProjectIdResponse) {
                view?.hideLoading()
                if (response.success) {
                    val data = response.data
                    view?.onResultSuccessGetProject(data)
                }
            }
        }
    }

    override fun updateHireByHrId(hrId: String?, status: String) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceHire?.updateStatus(hrId, status)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessUpdateHire()
                }
            }
        }
    }

    override fun getHireById(hrId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceHire.getHireById(hrId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is GetHireByIdResponse) {
                view?.hideLoading()
                if (response.success) {
                    val data = response.data
                    view?.onResultSuccessGetHire(data)
                }
            }
        }
    }

}