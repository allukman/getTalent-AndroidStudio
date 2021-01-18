package id.smartech.get_talent.activity.hire.list_hire

import id.smartech.get_talent.data.HireEngineerModel

interface GetHireContract {

    interface View {
        fun onResultSuccess(list: List<HireEngineerModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
        fun moveDetailHire()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getAllHireEngineer(enId: String?)
    }
}