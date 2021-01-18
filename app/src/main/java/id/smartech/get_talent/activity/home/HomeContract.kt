package id.smartech.get_talent.activity.home

import id.smartech.get_talent.data.EngineerModel

interface HomeContract {

    interface View {
        fun onResultSuccess(list: List<EngineerModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
        fun moveDetailEngineer()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getAllEngineer()
    }
}