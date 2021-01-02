package id.smartech.get_talent.activity.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.FragmentListProjectCompanyBinding
import id.smartech.get_talent.helper.ListProjectAdapter
import id.smartech.get_talent.service.ProjectApiService
import id.smartech.get_talent.data.ProjectCompanyModel
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class ListProjectCompanyFragment : Fragment() {

    private lateinit var binding: FragmentListProjectCompanyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_project_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)

        binding.rvListProject.adapter = ListProjectAdapter()
        binding.rvListProject.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        getProjectByComId()
        return binding.root
    }

    private fun getProjectByComId() {
        val service = ApiClient.getApiClient(requireContext())?.create(ProjectApiService::class.java)
        coroutineScope.launch {
            Log.d("project", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("project", "CallAPI : ${Thread.currentThread().name}")

                try {
                    service?.getProjectByComId(prefHelper.getInteger(Constant.COM_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("project response", response.toString())

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
                (binding.rvListProject.adapter as ListProjectAdapter).addList(list)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}