package id.smartech.get_talent.activity.profile.profile.engineer

import id.smartech.get_talent.activity.profile.detail_profile.engineer.DetailEngineerResponse
import id.smartech.get_talent.activity.skill.SkillModel

interface ProfileEngineerContract {

    interface View {
        fun onResultSuccessGetEngineer(data: DetailEngineerResponse.Data)
        fun onResultSuccessGetSkill(list: List<SkillModel>)
        fun onResultSuccessCreateSkill()
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getEngineerByEngId(enId: String?)
        fun getSkillByEngId(enId: String?)
        fun createSkill(enId: String?, skillName: String)
    }
}