package id.smartech.get_talent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.profile.editProfile.EditProfileCompanyActivity
import id.smartech.get_talent.activity.profile.detailProfile.DetailCompanyResponse
import id.smartech.get_talent.databinding.FragmentCompanyProfileBinding
import id.smartech.get_talent.helper.WebviewActivity
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.CompanyApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

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

    private lateinit var binding: FragmentCompanyProfileBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: CompanyApiService
    private lateinit var prefHelper: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_profile, container, false)
        prefHelper = PrefHelper(context = context)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)

        binding.companyGithub.setOnClickListener {
            val intent = Intent (activity, WebviewActivity::class.java)
            activity!!.startActivity(intent) }

        binding.btnEditCompany.setOnClickListener {
            val intent = Intent (getActivity(), EditProfileCompanyActivity::class.java)
            getActivity()!!.startActivity(intent) }

        getCompanyByComId()
        return binding.root
    }

    private fun getCompanyByComId(){
        val service = ApiClient.getApiClient(requireContext())?.create(CompanyApiService::class.java)
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service?.getCompanyById(prefHelper.getInteger(Constant.COM_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is DetailCompanyResponse) {
                if(response.success){
                    setProfile(response.data.companyEmail,
                                response.data.companyName,
                                response.data.companyBidang,
                                response.data.companyCity,
                                response.data.companyDescription,
                                response.data.companyInstagram,
                                response.data.companyGithub,
                                response.data.companyLinkedin,
                                response.data.companyPhoto)
                }
            }
        }
    }

    private fun setProfile(email: String, comName: String, comField: String, domisili: String, deskripsi: String, instagram: String, github: String, linkedin: String, photo: String){
        binding.companyEmail.text = email
        binding.companyName.text = comName
        binding.companyField.text = comField
        binding.comLocation.text = domisili
        binding.comDeskripsi.text = deskripsi
        binding.companyInstagram.text = instagram
        binding.companyGithub.text = github
        binding.companyLinkedin.text = linkedin

        val img = "http://174.129.47.146:4000/image/$photo"

        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(binding.profilePhoto)
    }


}