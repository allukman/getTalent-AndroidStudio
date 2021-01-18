package id.smartech.get_talent.activity.portofolio.detail_portofolio

import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse

interface DetailPortofolioContract {
    interface View {
        fun onResultSuccessGetPortofolio(data: GetPortofolioByPrIdResponse.Data)
        fun onResultSuccessDeletePortofolio()
        fun onResultFailed()
        fun showDialog()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getPortofolioByPrId(prId: String?)
        fun deletePortofolioByPrId(prId: String?)
    }
}