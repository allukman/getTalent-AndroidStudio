package id.smartech.get_talent.activity.experience.update_experience

import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse
import id.smartech.get_talent.activity.portofolio.response_portofolio.GetPortofolioByPrIdResponse
import okhttp3.MultipartBody

interface UpdateExperienceContract {
    interface View {
        fun onResultSuccessGetExperience(data: GetExperienceByXpIdResponse.Data)
        fun onResultSuccessUpdate(message: String)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getExperienceByXpId(xpId: String?)
        fun updateExperience(xpId: String, engineerId: String, jobPosition: String, companyName: String, exStart: String, exEnd: String, exDesc: String, photo: MultipartBody.Part)
        fun updateExperienceWithoutImage(xpId: String, engineerId: String, jobPosition: String, companyName: String, exStart: String, exEnd: String, exDesc: String)
    }
}