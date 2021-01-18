package id.smartech.get_talent.activity.profile.edit_profile.company

import android.content.Intent
import android.util.Log
import id.smartech.get_talent.activity.main.CompanyMainActivity
import id.smartech.get_talent.activity.profile.detail_profile.company.DetailCompanyResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.service.CompanyApiService
import id.smartech.get_talent.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditCompanyPresenter(private val coroutineScope: CoroutineScope,
                            private val companyService: CompanyApiService,
                            private val accountService: AccountService): EditCompanyContract.Presenter {

    private var view: EditCompanyContract.View? = null
    override fun bindToView(view: EditCompanyContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getCompanyByComId(comId: Int) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    companyService?.getCompanyById(comId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is DetailCompanyResponse) {
                view?.hideLoading()
                if (response.success) {
                    val data = response.data
                    view?.onResultSuccessGetCompany(data)
                }
            }
        }
    }

    override fun updateCompany(
        comId: Int,
        name: String,
        position: String,
        bidang: String,
        city: String,
        desc: String,
        ig: String,
        linkedin: String,
        github: String,
        photo: MultipartBody.Part
    ) {
        val companyName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val comPosition = position.toRequestBody("text/plain".toMediaTypeOrNull())
        val comBidang = bidang.toRequestBody("text/plain".toMediaTypeOrNull())
        val comLocation = city.toRequestBody("text/plain".toMediaTypeOrNull())
        val comDesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val instagram = ig.toRequestBody("text/plain".toMediaTypeOrNull())
        val comLinkedin = linkedin.toRequestBody("text/plain".toMediaTypeOrNull())
        val comGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    companyService?.updateCompany(comId, companyName, comPosition, comBidang, comLocation, comDesc, instagram, comLinkedin, comGithub, photo)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessUpdate("Success update data")
                }
            }
        }
    }

    override fun updateCompanyWithoutImage(
        comId: Int,
        name: String,
        position: String,
        bidang: String,
        city: String,
        desc: String,
        ig: String,
        linkedin: String,
        github: String
    ) {
        val companyName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val comPosition = position.toRequestBody("text/plain".toMediaTypeOrNull())
        val comBidang = bidang.toRequestBody("text/plain".toMediaTypeOrNull())
        val comLocation = city.toRequestBody("text/plain".toMediaTypeOrNull())
        val comDesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val instagram = ig.toRequestBody("text/plain".toMediaTypeOrNull())
        val comLinkedin = linkedin.toRequestBody("text/plain".toMediaTypeOrNull())
        val comGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    companyService?.updateCompanyWithoutImage(comId, companyName, comPosition, comBidang, comLocation, comDesc, instagram, comLinkedin, comGithub)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if (response.success) {
                    view?.onResultSuccessUpdate("Success update data")
                }
            }
        }
    }

    override fun updateAccount(
        accountId: String?,
        accountName: String,
        accountEmail: String,
        accountPhone: String,
        accountPassword: String,
        accountLevel: String
    ) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    accountService?.updateAccount(accountId, accountName, accountEmail, accountPhone, accountPassword, accountLevel)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is HelperResponse){
                if (response.success) {
                    view?.onResultSuccessUpdate("Success update data")
                }
            }
        }
    }
}