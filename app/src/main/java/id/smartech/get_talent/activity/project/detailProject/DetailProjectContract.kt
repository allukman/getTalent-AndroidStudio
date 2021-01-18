package id.smartech.get_talent.activity.project.detailProject

import id.smartech.get_talent.activity.hire.response_hire.GetHireByIdResponse
import id.smartech.get_talent.activity.project.response_project.ProjectIdResponse

interface DetailProjectContract {

    interface View {
        fun onResultSuccessGetProject(data: ProjectIdResponse.Data)
        fun onResultSuccessGetHire(data: GetHireByIdResponse.Data)
        fun onResultSuccessUpdateHire()
        fun onResultFailed()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: DetailProjectContract.View)
        fun unBind()
        fun getProjectByPjId(pjId: String?)
        fun updateHireByHrId(hrId: String?, status: String)
        fun getHireById(hrId: String?)
    }
}