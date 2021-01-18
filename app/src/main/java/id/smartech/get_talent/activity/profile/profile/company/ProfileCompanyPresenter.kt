package id.smartech.get_talent.activity.profile.profile.company

import id.smartech.get_talent.activity.profile.detail_profile.company.DetailCompanyResponse
import id.smartech.get_talent.service.CompanyApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileCompanyPresenter (private val coroutineScope: CoroutineScope,
                                private val service: CompanyApiService) : ProfileCompanyContract.Presenter {

    private var view: ProfileCompanyContract.View? = null

    override fun bindToView(view: ProfileCompanyContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getCompanyByComId(comId: Int) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getCompanyById(comId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is DetailCompanyResponse) {
                view?.hideLoading()
                if (response.success) {
                    val data = response.data
                    view?.onResultSuccessGetCompany(data)
                }
            }
        }
    }
}