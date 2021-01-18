package id.smartech.get_talent.activity.search

import id.smartech.get_talent.data.EngineerModel

interface SearchConstract {

    interface View {
        fun onResultSuccess(list: List<EngineerModel>)
        fun onResultFailed(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun searchEngineer(search: String?, filter: Int?)
    }
}