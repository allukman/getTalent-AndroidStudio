package id.smartech.get_talent.activity.project.listProject

import id.smartech.get_talent.data.ProjectCompanyModel
import id.smartech.get_talent.helper.ListProjectAdapter
import id.smartech.get_talent.service.ProjectApiService
import id.smartech.get_talent.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListProjectPresenter(private val coroutineScope: CoroutineScope,
                            private val service: ProjectApiService?): ListProjectContract.Presenter {

    private var view: ListProjectContract.View? = null

    override fun bindToView(view: ListProjectContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun callProjectApi(comId: Int) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByComId(comId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if(response is ProjectResponse) {
                val list = response.data.map {
                    ProjectCompanyModel(
                        it.projectId,
                        it.companyId,
                        it.projectName,
                        it.projectDesc,
                        it.projectDeadline,
                        it.projectImage,
                        it.projectCreateAt,
                        it.projectUpdateAt
                    )
                }
                view?.addListProject(list)
            }
        }
    }

}