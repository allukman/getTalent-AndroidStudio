package id.smartech.get_talent.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.EditProfileCompanyActivity
import id.smartech.get_talent.activity.EditProfileEngineerActivity
import id.smartech.get_talent.databinding.FragmentEngineerProfileBinding
import id.smartech.get_talent.helper.TabPagerAdaptor
import id.smartech.get_talent.helper.WebviewActivity
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

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
        binding.engGithub.setOnClickListener {
            val intent = Intent (getActivity(), WebviewActivity::class.java)
            getActivity()!!.startActivity(intent) }

        val prefs = getActivity()!!.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        binding.engName.text = prefs.getString("ACC_NAMA", null)
        binding.engEmail.text = prefs.getString("ACC_EMAIL", null)

        binding.btnEditEngineer.setOnClickListener {
            val intent = Intent (getActivity(), EditProfileEngineerActivity::class.java)
            getActivity()!!.startActivity(intent) }
        pagerAdapter = TabPagerAdaptor(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }


}