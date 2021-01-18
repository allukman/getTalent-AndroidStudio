package id.smartech.get_talent.activity.profile.detail_profile.engineer

import id.smartech.get_talent.activity.skill.SkillModel

interface DetailEngineerContract {

    interface View {
        fun onResultSuccessGetEngineer(data: DetailEngineerResponse.Data)
        fun onResultSuccessGetSkill(list: List<SkillModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getEngineerByEngId(enId: String?)
        fun getSkillByEngId(enId: String?)
    }

}