package id.smartech.get_talent.activity.experience

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.experience.createExperience.CreateExperienceActivity
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
class TalentExperienceFragment : Fragment(), OnRecyclerViewClickListener {

    private lateinit var binding: FragmentTalentExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: ExperienceApiService
    var listExperience = ArrayList<ExperienceModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talent_experience, container, false)
        coroutineScope = CoroutineScope(Job() +Dispatchers.Main)
        prefHelper = PrefHelper(context = context)
        service = ApiClient.getApiClient(requireContext())!!.create(ExperienceApiService::class.java)

        binding.experienceRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.experienceRecyclerView.adapter = ExperienceAdapter(listExperience, this)

        binding.btnAddExperience.setOnClickListener {
            val intent = Intent (activity, CreateExperienceActivity::class.java)
            activity!!.startActivity(intent)
        }

        getExperienceByEngId()
        return binding.root
    }

    private fun getExperienceByEngId(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getExperienceByEngId(prefHelper.getString(Constant.EN_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(response is GetExperienceByEngIdResponse) {
                val list = response.data.map {
                    ExperienceModel(
                        it.experienceId,
                        it.engineerId,
                        it.experiencePosisi,
                        it.experienceCompany,
                        it.experienceStart.split("-")[0],
                        it.experienceEnd.split("-")[0],
                        it.experienceDeskripsi,
                        it.experiencePhoto
                    )
                }
                (binding.experienceRecyclerView.adapter as ExperienceAdapter).addList(list)
            } else {
            Log.d("experience" , "failed to get Experience")
        }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.XP_ID_CLICK, listExperience[position].exId)
        Toast.makeText(requireContext(), prefHelper.getString(Constant.XP_ID_CLICK), Toast.LENGTH_SHORT).show()
        val intent = Intent (activity, DetailExperienceActivity::class.java)
        activity!!.startActivity(intent)
    }
}