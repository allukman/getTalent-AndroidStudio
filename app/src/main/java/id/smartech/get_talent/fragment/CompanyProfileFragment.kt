package id.smartech.get_talent.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.EditProfileCompanyActivity
import id.smartech.get_talent.databinding.FragmentCompanyProfileBinding
import id.smartech.get_talent.helper.WebviewActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompanyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompanyProfileFragment : Fragment() {

    lateinit var binding: FragmentCompanyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_profile, container, false)

        binding.companyGithub.setOnClickListener {
            val intent = Intent (activity, WebviewActivity::class.java)
            activity!!.startActivity(intent) }

        val prefs = getActivity()!!.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        binding.companyName.text = prefs.getString("COM_NAME", null)
        binding.companyEmail.text = prefs.getString("ACC_EMAIL", null)

        binding.btnEditCompany.setOnClickListener {
            val intent = Intent (getActivity(), EditProfileCompanyActivity::class.java)
            getActivity()!!.startActivity(intent) }
        return binding.root
    }


}