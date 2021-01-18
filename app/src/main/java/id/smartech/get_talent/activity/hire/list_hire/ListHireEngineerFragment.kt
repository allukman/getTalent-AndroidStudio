package id.smartech.get_talent.activity.hire.list_hire

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
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.activity.project.detailProject.DetailProjectActivity
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
class ListHireEngineerFragment : Fragment(), OnRecyclerViewClickListener, GetHireContract.View {

    private lateinit var binding: FragmentListHireEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: HireService
    private var listHireEngineer = ArrayList<HireEngineerModel>()
    private var presenter: GetHireContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)
        service = ApiClient.getApiClient(requireContext())!!.create(HireService::class.java)
        presenter = GetHirePresenter(coroutineScope, service)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val enId = prefHelper.getString(Constant.EN_ID)

        presenter?.getAllHireEngineer(enId)
        binding.rvListHire.adapter = ListHireAdapter(listHireEngineer, this)
        binding.rvListHire.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onResultSuccess(list: List<HireEngineerModel>) {
        (binding.rvListHire.adapter as ListHireAdapter).addList(list)
        binding.rvListHire.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvListHire.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.ivNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.rvListHire.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun moveDetailHire() {
        startActivity(Intent(activity, DetailProjectActivity::class.java))
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.HIRE_ID, listHireEngineer[position].hireId)
        prefHelper.put(Constant.PJ_ID_CLICK, listHireEngineer[position].projectId)
        moveDetailHire()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onStart() {
        val enId = prefHelper.getString(Constant.EN_ID)
        presenter?.bindToView(this)
        presenter?.getAllHireEngineer(enId)
        super.onStart()
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }


}