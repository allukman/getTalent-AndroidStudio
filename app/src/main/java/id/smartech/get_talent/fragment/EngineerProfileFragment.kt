package id.smartech.get_talent.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.EditProfileCompanyActivity
import id.smartech.get_talent.activity.EditProfileEngineerActivity
import id.smartech.get_talent.activity.detailProfile.DetailEngineerResponse
import id.smartech.get_talent.databinding.FragmentEngineerProfileBinding
import id.smartech.get_talent.helper.TabPagerAdaptor
import id.smartech.get_talent.helper.WebviewActivity
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*
import retrofit2.create

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
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_profile, container, false)
        prefHelper = PrefHelper(context = context)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        binding.engGithub.setOnClickListener {
            val intent = Intent (getActivity(), WebviewActivity::class.java)
            getActivity()!!.startActivity(intent) }

        binding.btnEditEngineer.setOnClickListener {
            val intent = Intent (getActivity(), EditProfileEngineerActivity::class.java)
            getActivity()!!.startActivity(intent) }
        pagerAdapter = TabPagerAdaptor(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        getEngineerByEngId()

        return binding.root
    }

    private fun getEngineerByEngId(){
        val service = ApiClient.getApiClient(requireContext())?.create(EngineerApiService::class.java)
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerByEngId(prefHelper.getString(Constant.EN_ID))
                } catch (e:Throwable){
                    e.printStackTrace()
                }
            }
            if (response is DetailEngineerResponse) {
                if (response.success) {
                    Toast.makeText(requireContext(), response.data.accountName, Toast.LENGTH_SHORT).show()
                    setProfile(response.data.accountName,
                        response.data.engineerJobTitle,
                        response.data.engineerDomisili,
                        response.data.engineerDeskripsi,
                        response.data.accountEmail,
                        response.data.engineerInstagram,
                        response.data.engineerGithub,
                        response.data.engineerGitlab,
                        response.data.engineerPhoto)
                }
            }
        }
    }

    private fun setProfile(engName: String, jobTitle: String, domisili: String, deskripsi: String, email: String, instagram: String, github: String, gitlab: String, photo: String){
        binding.engName.text = engName
        binding.engJobTitle.text = jobTitle
        binding.engLocation.text = domisili
        binding.engDeskripsi.text = deskripsi
        binding.engEmail.text = email
        binding.engInstagram.text = instagram
        binding.engGithub.text = github
        binding.engGitlab.text = gitlab

        val img = "http://174.129.47.146:4000/image/$photo"

        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(binding.profilePhoto)
    }
}