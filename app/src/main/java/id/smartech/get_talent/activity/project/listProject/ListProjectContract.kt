package id.smartech.get_talent.activity.project.listProject

import id.smartech.get_talent.data.ProjectCompanyModel

interface ListProjectContract {

    interface View {
        fun addListProject(list: List<ProjectCompanyModel>)
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun callProjectApi(comId: Int)
    }
}