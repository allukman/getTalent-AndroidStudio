package id.smartech.get_talent.activity.profile.edit_profile.engineer

import id.smartech.get_talent.activity.profile.detail_profile.engineer.DetailEngineerResponse
import id.smartech.get_talent.activity.skill.SkillModel
import okhttp3.MultipartBody

interface EditEngineerContract {

    interface View {
        fun onResultSuccessGetEngineer(data: DetailEngineerResponse.Data)
        fun onResultSuccessUpdate(message: String)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getEngineerByEngId(enId: String?)
        fun updateEngineer(engineerId: String, jobTitle: String, jobType: String, domisili: String, deskripsi: String, instagram: String, github: String, gitlab: String, photo: MultipartBody.Part)
        fun updateEngineerWithoutImage(engineerId: String, jobTitle: String, jobType: String, domisili: String, deskripsi: String, instagram: String, github: String, gitlab: String)
        fun updateAccount(accountId: String?, accountName: String, accountEmail: String, accountPhone: String, accountPassword: String, accountLevel: String)
    }
}