package id.smartech.get_talent.activity.experience.get_experience

import id.smartech.get_talent.data.ExperienceModel

interface GetExperienceContract {

    interface View {
        fun onResultSuccess(list: List<ExperienceModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
        fun moveAddExperience()
        fun moveDetailExperience()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getAllExperienceEngineer(enId: String?)
    }
}