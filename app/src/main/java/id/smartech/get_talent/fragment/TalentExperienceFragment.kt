package id.smartech.get_talent.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import id.smartech.get_talent.R
import id.smartech.get_talent.data.Experience
import id.smartech.get_talent.databinding.FragmentTalentExperienceBinding
import id.smartech.get_talent.helper.ExperienceRecyclerViewAdapter

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talent_experience, container, false)

        val loremIpsum = getString(R.string.lorem_ipsum)

        val data = listOf<Experience>(
            Experience(R.drawable.tokopedia, "Web developer", "Google", "10 january 2019 - 9 desember 2020", loremIpsum),
            Experience(R.drawable.tokopedia, "Engineer", "Tokopedia", "10 january 2019 - 9 desember 2020", loremIpsum),
            Experience(R.drawable.tokopedia, "Android developer", "Instagram", "10 january 2019 - 9 desember 2020", loremIpsum),
            Experience(R.drawable.tokopedia, "Web developer", "Google", "10 january 2019 - 9 desember 2020", loremIpsum)
        )

        binding.experienceRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.experienceRecyclerView.setHasFixedSize(true)
        binding.experienceRecyclerView.adapter = ExperienceRecyclerViewAdapter(data)

        return binding.root
    }
}