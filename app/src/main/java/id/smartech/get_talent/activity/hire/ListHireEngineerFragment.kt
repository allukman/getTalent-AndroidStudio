package id.smartech.get_talent.activity.hire

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
import id.smartech.get_talent.data.HireEngineerModel
import id.smartech.get_talent.databinding.FragmentListHireEngineerBinding
import id.smartech.get_talent.helper.ListHireAdapter
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.HireService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListHireEngineerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListHireEngineerFragment : Fragment() {
    private lateinit var binding: FragmentListHireEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)

        binding.rvListHire.adapter = ListHireAdapter()
        binding.rvListHire.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        getHireEngineer()
        return binding.root
    }

    private fun getHireEngineer(){
        val service = ApiClient.getApiClient(requireContext())?.create(HireService::class.java)

        coroutineScope.launch {
            Log.d("hire", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("project", "CallAPI : ${Thread.currentThread().name}")

                try {
                    service?.getHireEngineer(prefHelper.getString(Constant.EN_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("project response", response.toString())

            if(response is HireResponse) {
                val list = response.data.map {
                    HireEngineerModel(
                        it.hireId,
                        it.engineerId,
                        it.projectId,
                        it.hirePrice,
                        it.projectName,
                        it.hireMessage,
                        it.hireStatus,
                        it.hireCreated,
                        it.projectDeadline
                    )
                }
                (binding.rvListHire.adapter as ListHireAdapter).addList(list)

            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}