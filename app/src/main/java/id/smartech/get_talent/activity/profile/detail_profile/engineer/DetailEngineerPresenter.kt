package id.smartech.get_talent.activity.profile.detail_profile.engineer

import id.smartech.get_talent.activity.skill.GetSkillResponse
import id.smartech.get_talent.activity.skill.SkillModel
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.service.SkillApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DetailEngineerPresenter (private val coroutineScope: CoroutineScope,
                                private val engineerService: EngineerApiService,
                                private val skillService: SkillApiService) : DetailEngineerContract.Presenter {

    private var view: DetailEngineerContract.View? = null

    override fun bindToView(view: DetailEngineerContract.View) {
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

    override fun getSkillByEngId(enId: String?) {
        coroutineScope.launch {
            view?.showLoading()
            val response = withContext(Dispatchers.IO) {
                try {
                    skillService.getSkill(enId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()
                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("you don't have a skill")
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

            if(response is GetSkillResponse) {
                view?.hideLoading()
                if (response.success) {
                    val list = response.data.map {
                        SkillModel(it.skillId, it.engineerId, it.skillName)
                    }
                    view?.onResultSuccessGetSkill(list)
                } else {
                    view?.onResultFail(response.message)
                }

            }
        }
    }

}