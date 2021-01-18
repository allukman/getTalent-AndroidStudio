package id.smartech.get_talent.activity.portofolio.get_portofolio

import id.smartech.get_talent.data.PortofolioModel


interface GetPortofolioContract {
    interface View {
        fun onResultSuccess(list: List<PortofolioModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
        fun moveAddPortofolio()
        fun moveDetailPortofolio()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getAllPortofolioEngineer(enId: String?)
    }
}