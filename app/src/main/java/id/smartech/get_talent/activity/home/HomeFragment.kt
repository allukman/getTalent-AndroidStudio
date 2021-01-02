package id.smartech.get_talent.activity.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.ErrorActivity
import id.smartech.get_talent.R
import id.smartech.get_talent.data.HomeModel
import id.smartech.get_talent.databinding.FragmentHomeBinding
import id.smartech.get_talent.helper.HomeAdapter
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)

        val accountName = prefHelper.getString(Constant.ACC_NAMA)
        binding.tvGreeting.text = "Hai, $accountName"

        binding.rvJobTitle1.adapter = HomeAdapter()
        binding.rvJobTitle1.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        binding.rvJobTitle2.adapter = HomeAdapter()
        binding.rvJobTitle2.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        getEngineerAndroidDeveloper()
        getEngineerWebDeveloper()
        return binding.root
    }

    private fun getEngineerWebDeveloper() {
        val service = ApiClient.getApiClient(requireContext())?.create(EngineerApiService::class.java)

        coroutineScope.launch {
            Log.d("webdev", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("webdev", "CallAPI : ${Thread.currentThread().name}")

                try {
                    service?.getEngineerWebDeveloper()
                } catch (e:Throwable) {
                    e.printStackTrace()

                }
            }

            Log.d("webdev response", response.toString())

            if(response is EngineerResponse) {
                val list = response.data?.map {
                    HomeModel(
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
                val intent = Intent (activity, ErrorActivity::class.java)
                activity!!.startActivity(intent)
            }
        }
    }

    private fun getEngineerAndroidDeveloper() {
        val service = ApiClient.getApiClient(requireContext())?.create(EngineerApiService::class.java)

        coroutineScope.launch {
            Log.d("android", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("android", "CallAPI : ${Thread.currentThread().name}")

                try {
                    service?.getEngineerAndroidDeveloper()
                } catch (e:Throwable) {
                    e.printStackTrace()

                }
            }

            Log.d("android response", response.toString())

            if(response is EngineerResponse) {
                val list = response.data?.map {
                    HomeModel(
                        it.engineerId,
                        it.accountId,
                        it.accountName,
                        it.engineerJobTitle,
                        it.engineerJobType,
                        it.engineerDomicile,
                        it.engineerPhoto
                    )
                }
                (binding.rvJobTitle2.adapter as HomeAdapter).addList(list)
            }
        }
    }
    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }


}