package id.smartech.get_talent.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.FragmentEngineerProfileBinding
import id.smartech.get_talent.helper.TabPagerAdaptor

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EngineerProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EngineerProfileFragment : Fragment() {

    private lateinit var binding: FragmentEngineerProfileBinding
    private lateinit var pagerAdapter: TabPagerAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_profile, container, false)


        pagerAdapter = TabPagerAdaptor(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }


}