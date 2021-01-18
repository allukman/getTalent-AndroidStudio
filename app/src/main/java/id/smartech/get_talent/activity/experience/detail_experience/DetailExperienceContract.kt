package id.smartech.get_talent.activity.experience.detail_experience

import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByXpIdResponse

interface DetailExperienceContract {

    interface View {
        fun onResultSuccessGetExperience(data: GetExperienceByXpIdResponse.Data)
        fun onResultSuccessDeleteExperience()
        fun onResultFailed()
        fun showDialog()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getExperienceByXpId(xpId: String?)
        fun deleteExperienceByXpId(xpId: String?)
    }
}