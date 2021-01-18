package id.smartech.get_talent.activity.profile.profile.company

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.profile.edit_profile.company.EditProfileCompanyActivity
import id.smartech.get_talent.activity.profile.detail_profile.company.DetailCompanyResponse
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
class CompanyProfileFragment : Fragment(), ProfileCompanyContract.View {

    private lateinit var binding: FragmentCompanyProfileBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: CompanyApiService
    private lateinit var prefHelper: PrefHelper
    private var presenter: ProfileCompanyContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_profile, container, false)
        prefHelper = PrefHelper(context = context)
        coroutineScope = CoroutineScope(Job()+ Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(CompanyApiService::class.java)
        presenter = ProfileCompanyPresenter(coroutineScope, service)

        binding.companyGithub.setOnClickListener {
            val intent = Intent (activity, WebviewActivity::class.java)
            activity!!.startActivity(intent) }

        binding.btnEditCompany.setOnClickListener {
            val intent = Intent (activity, EditProfileCompanyActivity::class.java)
            activity!!.startActivity(intent)
        }

        return binding.root
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



    override fun onResultSuccessGetCompany(data: DetailCompanyResponse.Data) {
        setProfile(data.companyEmail, data.companyName, data.companyBidang, data.companyCity, data.companyDescription,
        data.companyInstagram, data.companyGithub, data.companyLinkedin, data.companyPhoto)
    }

    override fun showLoading() {
        binding.companyView.visibility = View.GONE
        binding.companyView2.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.companyView.visibility = View.VISIBLE
        binding.companyView2.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        val comId = prefHelper.getInteger(Constant.COM_ID)

        presenter?.bindToView(this)
        presenter?.getCompanyByComId(comId)
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }


}