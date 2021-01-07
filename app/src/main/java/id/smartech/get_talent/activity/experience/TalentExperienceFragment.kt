package id.smartech.get_talent.activity.experience

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
import id.smartech.get_talent.data.Experience
import id.smartech.get_talent.data.ExperienceModel
import id.smartech.get_talent.databinding.FragmentTalentExperienceBinding
import id.smartech.get_talent.helper.ExperienceAdapter
import id.smartech.get_talent.helper.ExperienceRecyclerViewAdapter
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
class TalentExperienceFragment : Fragment() {

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

//        val loremIpsum = getString(R.string.lorem_ipsum)
//        val data = listOf<Experience>(
//            Experience(R.drawable.tokopedia, "Web developer", "Google", "10 january 2019 - 9 desember 2020", loremIpsum),
//            Experience(R.drawable.tokopedia, "Engineer", "Tokopedia", "10 january 2019 - 9 desember 2020", loremIpsum),
//            Experience(R.drawable.tokopedia, "Android developer", "Instagram", "10 january 2019 - 9 desember 2020", loremIpsum),
//            Experience(R.drawable.tokopedia, "Web developer", "Google", "10 january 2019 - 9 desember 2020", loremIpsum)
//        )

        binding.experienceRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.experienceRecyclerView.adapter = ExperienceAdapter()

        getExperienceByEngId()
        return binding.root
    }

    private fun getExperienceByEngId(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getExperienceByEngId(prefHelper.getString(Constant.EN_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(response is GetExperienceByEngIdResponse) {
                val list = response.data?.map {
                    ExperienceModel(
                        it.experienceId,
                        it.engineerId,
                        it.experiencePosisi,
                        it.experienceCompany,
                        it.experienceStart,
                        it.experienceEnd,
                        it.experienceDeskripsi,
                        it.experiencePhoto
                    )
                }
                (binding.experienceRecyclerView.adapter as ExperienceAdapter).addList(list)
            } else {
                Log.d("experience" , "experience tidak ada")
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}