package id.smartech.get_talent.activity.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.activity.profile.detail_profile.engineer.ProfileEngineerActivity
import id.smartech.get_talent.data.EngineerModel
import id.smartech.get_talent.databinding.FragmentSearchBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), SearchConstract.View, OnRecyclerViewClickListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService
    private lateinit var prefHelper: PrefHelper
    var listEngineer = ArrayList<EngineerModel>()

    private var presenter: SearchPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)
        prefHelper = PrefHelper(requireContext())

        presenter = SearchPresenter(coroutineScope, service)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearch.adapter = SearchAdapter(listEngineer, this)
        binding.rvSearch.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        binding.ivFilter.setOnClickListener {
            selectFilter()
        }

        setSearchView()
        setRecylerView()
    }

    private fun selectFilter() {
        val builder: AlertDialog.Builder? = activity?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Filter")
        builder?.setIcon(R.drawable.ic_filter)

        val user = arrayOf("Name", "Domicile", "freelance", "full time")
        builder?.setItems(user) { _, which ->
            when (which) {
                0 -> {
                    Toast.makeText(activity, "Filter by Name",Toast.LENGTH_LONG).show()
                    presenter?.searchEngineer(null, 0)
                }
                1 -> {
                    Toast.makeText(activity, "Filter by domicile",Toast.LENGTH_LONG).show()
                    presenter?.searchEngineer(null, 1)
                }
                2 -> {
                    Toast.makeText(activity, "filter by job type freelance",Toast.LENGTH_LONG).show()
                    presenter?.searchEngineer(null, 2)
                }
                3 -> {
                    Toast.makeText(activity, "filter by job type full time",Toast.LENGTH_LONG).show()
                    presenter?.searchEngineer(null, 3)
                }
            }
        }?.show()
    }

    override fun onResultSuccess(list: List<EngineerModel>) {
        (binding.rvSearch.adapter as SearchAdapter).addList(list)
        binding.rvSearch.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun onResultFailed(message: String) {
        binding.rvSearch.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.ivNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setSearchView() {
        binding.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
        android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    presenter?.searchEngineer(null, null)
                } else {
                    if (newText?.length!! >= 3) {
                        presenter?.searchEngineer(newText, null)
                    }
                }
                return true
            }
        })
    }

    private fun setRecylerView() {
        binding.rvSearch.isNestedScrollingEnabled = false
        binding.rvSearch.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        val adapter = SearchAdapter(listEngineer, this)
        binding.rvSearch.adapter = adapter

    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.EN_ID_CLICK, listEngineer[position].engineerId)
        val intent = Intent (activity, ProfileEngineerActivity::class.java)
        activity!!.startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
        presenter?.searchEngineer(null, null)
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }
}