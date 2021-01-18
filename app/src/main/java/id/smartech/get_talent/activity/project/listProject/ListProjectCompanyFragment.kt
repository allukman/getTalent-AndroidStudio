package id.smartech.get_talent.activity.project.listProject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.activity.project.detailProject.DetailProjectActivity
import id.smartech.get_talent.activity.project.createProject.CreateProjectActivity
import id.smartech.get_talent.helper.ListProjectAdapter
import id.smartech.get_talent.service.ProjectApiService
import id.smartech.get_talent.data.ProjectCompanyModel
import id.smartech.get_talent.databinding.FragmentListProjectCompanyBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class ListProjectCompanyFragment : Fragment(), OnRecyclerViewClickListener, ListProjectContract.View {

    private lateinit var binding: FragmentListProjectCompanyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: ProjectApiService
    private var presenter: ListProjectPresenter? = null
    var listProject = ArrayList<ProjectCompanyModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_project_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)
        service = ApiClient.getApiClient(requireContext())!!.create(ProjectApiService::class.java)
        presenter = ListProjectPresenter(coroutineScope, service)

        binding.btnAddProjectCompany.setOnClickListener {
            val intent = Intent (activity, CreateProjectActivity::class.java)
            activity!!.startActivity(intent)
        }

        binding.rvListProject.adapter = ListProjectAdapter(listProject, this)
        binding.rvListProject.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        return binding.root
    }

//    private fun getProjectByComId() {
//        val service = ApiClient.getApiClient(requireContext())?.create(ProjectApiService::class.java)
//        coroutineScope.launch {
//            val response = withContext(Dispatchers.IO) {
//                try {
//                    service?.getProjectByComId(prefHelper.getInteger(Constant.COM_ID))
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//            if(response is ProjectResponse) {
//                val list = response.data.map {
//                    ProjectCompanyModel(
//                        it.projectId,
//                        it.companyId,
//                        it.projectName,
//                        it.projectDesc,
//                        it.projectDeadline,
//                        it.projectImage,
//                        it.projectCreateAt,
//                        it.projectUpdateAt
//                    )
//                }
//                (binding.rvListProject.adapter as ListProjectAdapter).addList(list)
//            }
//        }
//    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.PJ_ID_CLICK, listProject[position].projectId)
        Toast.makeText(activity, Constant.PJ_ID_CLICK, Toast.LENGTH_SHORT).show()
        moveIntent()
    }

    private fun moveIntent(){
        val intent = Intent (activity, DetailProjectActivity::class.java)
        activity!!.startActivity(intent)
    }

    override fun addListProject(list: List<ProjectCompanyModel>) {
        (binding.rvListProject.adapter as ListProjectAdapter).addList(list)
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
        presenter?.callProjectApi(prefHelper.getInteger(Constant.COM_ID))
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroyView() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroyView()
    }


}