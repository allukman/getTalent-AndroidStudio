package id.smartech.get_talent.activity.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.ErrorActivity
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.profile.detailProfile.ProfileEngineerActivity
import id.smartech.get_talent.data.EngineerModel
import id.smartech.get_talent.databinding.FragmentHomeBinding
import id.smartech.get_talent.helper.HomeAdapter
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*


class HomeFragment : Fragment(), OnRecyclerViewClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    var listEngineer = ArrayList<EngineerModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)

        val accountName = prefHelper.getString(Constant.ACC_NAMA)
        binding.tvGreeting.text = "Hai, $accountName"

        binding.rvJobTitle1.adapter = HomeAdapter(listEngineer, this)
        binding.rvJobTitle1.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        getEngineerWebDeveloper()
        return binding.root
    }

    private fun getEngineerWebDeveloper() {
        val service = ApiClient.getApiClient(requireContext())?.create(EngineerApiService::class.java)

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerWebDeveloper()
                } catch (e:Throwable) {
                    e.printStackTrace()

                }
            }
            if(response is EngineerResponse) {
                val list = response.data?.map {
                    EngineerModel(
                        it.engineerId,
                        it.accountId,
                        it.accountName,
                        it.engineerJobTitle,
                        it.engineerJobType,
                        it.engineerDomicile,
                        it.engineerPhoto
                    )
                }
                (binding.rvJobTitle1.adapter as HomeAdapter).addList(list)
            } else {
                moveIntentError()
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.EN_ID_CLICK, listEngineer[position].engineerId)
        val intent = Intent (activity, ProfileEngineerActivity::class.java)
        activity!!.startActivity(intent)
    }

    private fun moveIntentError(){
        val intent = Intent (activity, ErrorActivity::class.java)
        activity!!.startActivity(intent)
    }

}