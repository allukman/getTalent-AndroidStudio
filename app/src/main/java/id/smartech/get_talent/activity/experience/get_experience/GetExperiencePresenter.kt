package id.smartech.get_talent.activity.experience.get_experience

import id.smartech.get_talent.activity.experience.response_experience.GetExperienceByEngIdResponse
import id.smartech.get_talent.data.ExperienceModel
import id.smartech.get_talent.service.ExperienceApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GetExperiencePresenter(private val coroutineScope: CoroutineScope,
                            private val service: ExperienceApiService): GetExperienceContract.Presenter {

    private var view: GetExperienceContract.View? = null

    override fun bindToView(view: GetExperienceContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun getAllExperienceEngineer(enId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getExperienceByEngId(enId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()
                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("you don't have a Experience")
                            }
                            e.code() == 400 -> {
                                view?.onResultFail("Please re-login")
                            }
                            else -> {
                                view?.onResultFail("Server under maintenance")
                            }
                        }
                    }
                }
            }

            if(response is GetExperienceByEngIdResponse) {
                view?.hideLoading()
                if (response.success) {
                    val list = response.data.map {
                        ExperienceModel(
                            it.experienceId,
                            it.engineerId,
                            it.experiencePosisi,
                            it.experienceCompany,
                            it.experienceStart.split("-")[0],
                            it.experienceEnd.split("-")[0],
                            it.experienceDeskripsi,
                            it.experiencePhoto
                        )
                    }
                    view?.onResultSuccess(list)
                } else {
                    view?.onResultFail(response.message)
                }

            }
        }
    }
}