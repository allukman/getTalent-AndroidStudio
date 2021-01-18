package id.smartech.get_talent.activity.portofolio.update_portofolio

import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import okhttp3.MultipartBody

interface UpdatePortofolioContract {

    interface View {
        fun onResultSuccessGetPortofolio(data: GetPortofolioByPrIdResponse.Data)
        fun onResultSuccessUpdate(message: String)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getPortofolioByPrId(prId: String?)
        fun updatePortofolio(prId: String, engineerId: String, appName: String, appDesc: String, appPub: String, appRepo: String, appTpKerja: String, appType: String, photo: MultipartBody.Part)
        fun updatePortofolioWithoutImage(prId: String, engineerId: String, appName: String, appDesc: String, appPub: String, appRepo: String, appTpKerja: String, appType: String)
    }
}