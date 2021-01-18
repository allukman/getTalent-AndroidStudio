package id.smartech.get_talent.activity.portofolio.get_portofolio

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
import id.smartech.get_talent.activity.portofolio.detail_portofolio.DetailPortofolioActivity
import id.smartech.get_talent.activity.portofolio.create_portofolio.CreatePortofolioActivity
import id.smartech.get_talent.data.PortofolioModel
import id.smartech.get_talent.databinding.FragmentTalentPortofolioBinding
import id.smartech.get_talent.helper.PortofolioAdapter
import id.smartech.get_talent.helper.PortofolioClickListener
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.PortofolioApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TalentPortofolioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TalentPortofolioFragment : Fragment(), PortofolioClickListener, GetPortofolioContract.View {

    private lateinit var binding: FragmentTalentPortofolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: PortofolioApiService
    private var listPortofolio = ArrayList<PortofolioModel>()
    private var presenter: GetPortofolioContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talent_portofolio, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(requireContext())
        service = ApiClient.getApiClient(requireContext())!!.create(PortofolioApiService::class.java)
        presenter = GetPortofolioPresenter(coroutineScope, service)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPortofolio.setOnClickListener {
            moveAddPortofolio()
        }

        val enId = prefHelper.getString(Constant.EN_ID)

        presenter?.getAllPortofolioEngineer(enId)
        binding.portofolioRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.portofolioRecyclerView.adapter = PortofolioAdapter(listPortofolio, this)

    }


    override fun onResultSuccess(list: List<PortofolioModel>) {
        (binding.portofolioRecyclerView.adapter as PortofolioAdapter).addList(list)
        binding.portofolioRecyclerView.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.portofolioRecyclerView.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.ivNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.portofolioRecyclerView.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun moveAddPortofolio() {
        val intent = Intent (activity, CreatePortofolioActivity::class.java)
        activity!!.startActivity(intent)
    }

    override fun moveDetailPortofolio() {
        val intent = Intent (activity, DetailPortofolioActivity::class.java)
        activity!!.startActivity(intent)
    }

    override fun onPortofoliItemClicked(position: Int) {
        prefHelper.put(Constant.PR_ID_CLICK, listPortofolio[position].prId)
        moveDetailPortofolio()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onStart() {
        val enId = prefHelper.getString(Constant.EN_ID)
        presenter?.bindToView(this)
        presenter?.getAllPortofolioEngineer(enId)
        super.onStart()
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

}