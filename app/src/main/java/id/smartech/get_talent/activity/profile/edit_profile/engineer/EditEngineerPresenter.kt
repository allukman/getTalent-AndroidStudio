package id.smartech.get_talent.activity.profile.edit_profile.engineer

import android.content.Intent
import android.util.Log
import android.widget.Toast
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.profile.detail_profile.engineer.DetailEngineerResponse
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.service.EngineerApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditEngineerPresenter(private val coroutineScope: CoroutineScope,
                            private val engineerService: EngineerApiService,
                            private val accountService: AccountService): EditEngineerContract.Presenter {

    private var view: EditEngineerContract.View? = null

    override fun bindToView(view: EditEngineerContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getEngineerByEngId(enId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    engineerService?.getEngineerByEngId(enId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is DetailEngineerResponse) {
                view?.hideLoading()
                if (response.success) {
                    val data = response.data
                    view?.onResultSuccessGetEngineer(data)
                }
            }
        }
    }

    override fun updateEngineer(
        engineerId: String,
        jobTitle: String,
        jobType: String,
        domisili: String,
        deskripsi: String,
        instagram: String,
        github: String,
        gitlab: String,
        photo: MultipartBody.Part
    ) {
        val title = jobTitle.toRequestBody("text/plain".toMediaTypeOrNull())
        val jobtype = jobType.toRequestBody("text/plain".toMediaTypeOrNull())
        val loc = domisili.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
        val ig = instagram.toRequestBody("text/plain".toMediaTypeOrNull())
        val enGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())
        val enGitlab = gitlab.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    engineerService?.updateEngineer(engineerId, title, jobtype,loc, desc, ig, enGithub, enGitlab, photo )
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

    override fun updateEngineerWithoutImage(
        engineerId: String,
        jobTitle: String,
        jobType: String,
        domisili: String,
        deskripsi: String,
        instagram: String,
        github: String,
        gitlab: String
    ) {
        val title = jobTitle.toRequestBody("text/plain".toMediaTypeOrNull())
        val jobtype = jobType.toRequestBody("text/plain".toMediaTypeOrNull())
        val loc = domisili.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
        val ig = instagram.toRequestBody("text/plain".toMediaTypeOrNull())
        val enGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())
        val enGitlab = gitlab.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    engineerService?.updateEngineerWithoutImage(engineerId, title, jobtype,loc, desc, ig, enGithub, enGitlab)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HelperResponse) {
                if(response.success) {
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