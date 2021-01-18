package id.smartech.get_talent.activity.experience.get_experience

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
import id.smartech.get_talent.activity.experience.detail_experience.DetailExperienceActivity
import id.smartech.get_talent.activity.experience.create_experience.CreateExperienceActivity
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.data.ExperienceModel
import id.smartech.get_talent.databinding.FragmentTalentExperienceBinding
import id.smartech.get_talent.helper.ExperienceAdapter
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.ExperienceApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TalentExperience.newInstance] factory method to
 * create an instance of this fragment.
 */
class TalentExperienceFragment : Fragment(), OnRecyclerViewClickListener, GetExperienceContract.View {

    private lateinit var binding: FragmentTalentExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ExperienceApiService
    private lateinit var prefHelper: PrefHelper
    private var listExperience = ArrayList<ExperienceModel>()
    private var presenter: GetExperienceContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talent_experience, container, false)
        coroutineScope = CoroutineScope(Job() +Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(ExperienceApiService::class.java)
        prefHelper = PrefHelper(requireContext())
        presenter = GetExperiencePresenter(coroutineScope, service)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddExperience.setOnClickListener {
            moveAddExperience()
        }

        val enId = prefHelper.getString(Constant.EN_ID)

        presenter?.getAllExperienceEngineer(enId)
        binding.experienceRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.experienceRecyclerView.adapter = ExperienceAdapter(listExperience, this)
    }

    override fun onResultSuccess(list: List<ExperienceModel>) {
        (binding.experienceRecyclerView.adapter as ExperienceAdapter).addList(list)
        binding.experienceRecyclerView.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.experienceRecyclerView.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.ivNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
        binding.experienceRecyclerView.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.ivNotFound.visibility = View.GONE
    }

    override fun moveAddExperience() {
        val intent = Intent (activity, CreateExperienceActivity::class.java)
        activity!!.startActivity(intent)
    }

    override fun moveDetailExperience() {
        val intent = Intent (activity, DetailExperienceActivity::class.java)
        activity!!.startActivity(intent)
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.XP_ID_CLICK, listExperience[position].exId)
        moveDetailExperience()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onStart() {
        val enId = prefHelper.getString(Constant.EN_ID)
        presenter?.bindToView(this)
        presenter?.getAllExperienceEngineer(enId)
        super.onStart()
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }
}