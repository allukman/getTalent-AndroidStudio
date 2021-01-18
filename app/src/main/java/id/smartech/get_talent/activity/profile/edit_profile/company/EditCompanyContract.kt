package id.smartech.get_talent.activity.profile.edit_profile.company

import id.smartech.get_talent.activity.profile.detail_profile.company.DetailCompanyResponse
import id.smartech.get_talent.activity.profile.detail_profile.engineer.DetailEngineerResponse
import okhttp3.MultipartBody

interface EditCompanyContract {

    interface View {
        fun onResultSuccessGetCompany(data: DetailCompanyResponse.Data)
        fun onResultSuccessUpdate(message: String)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun getCompanyByComId(comId: Int)
        fun updateCompany(comId: Int, name: String, position: String, bidang: String, city: String, desc: String, ig: String, linkedin: String, github: String, photo: MultipartBody.Part)
        fun updateCompanyWithoutImage(comId: Int, name: String, position: String, bidang: String, city: String, desc: String, ig: String, linkedin: String, github: String)
        fun updateAccount(accountId: String?, accountName: String, accountEmail: String, accountPhone: String, accountPassword: String, accountLevel: String)
    }
}