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
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.profile.detail_profile.engineer.ProfileEngineerActivity
import id.smartech.get_talent.data.EngineerModel
import id.smartech.get_talent.databinding.FragmentHomeBinding
import id.smartech.get_talent.helper.HomeAdapter
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*


class HomeFragment : Fragment(), OnRecyclerViewClickListener, HomeContract.View {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: EngineerApiService
    private var listEngineer = ArrayList<EngineerModel>()
    private var presenter: HomeContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)
        service = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)
        presenter = HomePresenter(coroutineScope, service)

        val accountName = prefHelper.getString(Constant.ACC_NAMA)
        binding.tvGreeting.text = "Hai, $accountName"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter?.getAllEngineer()

        binding.rvJobTitle1.adapter = HomeAdapter(listEngineer, this)
        binding.rvJobTitle1.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onResultSuccess(list: List<EngineerModel>) {
        (binding.rvJobTitle1.adapter as HomeAdapter).addList(list)
        binding.rvJobTitle1.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvJobTitle1.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.ivNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.rvJobTitle1.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun moveDetailEngineer() {
        val intent = Intent (activity, ProfileEngineerActivity::class.java)
        activity!!.startActivity(intent)
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.EN_ID_CLICK, listEngineer[position].engineerId)
        moveDetailEngineer()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onStart() {
        presenter?.bindToView(this)
        presenter?.getAllEngineer()
        super.onStart()
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

}