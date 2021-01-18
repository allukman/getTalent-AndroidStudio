package id.smartech.get_talent.activity.profile.profile.company

import id.smartech.get_talent.activity.profile.detail_profile.company.DetailCompanyResponse

interface ProfileCompanyContract {

    interface View {
        fun onResultSuccessGetCompany(data: DetailCompanyResponse.Data)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getCompanyByComId(comId: Int)
    }

}