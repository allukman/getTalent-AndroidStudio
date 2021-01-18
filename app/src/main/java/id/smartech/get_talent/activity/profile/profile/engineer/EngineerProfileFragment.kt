package id.smartech.get_talent.activity.profile.profile.engineer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.activity.profile.edit_profile.engineer.EditProfileEngineerActivity
import id.smartech.get_talent.activity.profile.detail_profile.engineer.DetailEngineerResponse
import id.smartech.get_talent.activity.skill.SkillAdapter
import id.smartech.get_talent.activity.skill.SkillModel
import id.smartech.get_talent.activity.skill.UpdateSkillActivity
import id.smartech.get_talent.databinding.FragmentEngineerProfileBinding
import id.smartech.get_talent.helper.TabPagerAdaptor
import id.smartech.get_talent.helper.WebviewActivity
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.service.SkillApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EngineerProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EngineerProfileFragment : Fragment(), OnRecyclerViewClickListener ,
    ProfileEngineerContract.View {

    private lateinit var binding: FragmentEngineerProfileBinding
    private lateinit var pagerAdapter: TabPagerAdaptor
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var engineerService: EngineerApiService
    private lateinit var skillService: SkillApiService
    private var presenter: ProfileEngineerContract.Presenter? = null
    private var listSkill = ArrayList<SkillModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_profile, container, false)
        prefHelper = PrefHelper(requireContext())
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        engineerService = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)
        skillService = ApiClient.getApiClient(requireContext())!!.create(SkillApiService::class.java)
        presenter =
            ProfileEngineerPresenter(
                coroutineScope,
                engineerService,
                skillService
            )

        pagerAdapter = TabPagerAdaptor(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.rvListSkill.adapter = SkillAdapter(listSkill, this)

        val recyclerView = binding.rvListSkill
        val layoutManager = FlexboxLayoutManager(activity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        recyclerView.layoutManager = layoutManager

        binding.engGithub.setOnClickListener {
            val intent = Intent (activity, WebviewActivity::class.java)
            activity!!.startActivity(intent) }

        binding.btnEditEngineer.setOnClickListener {
            val intent = Intent (activity, EditProfileEngineerActivity::class.java)
            activity!!.startActivity(intent) }

        binding.btnAddSkill.setOnClickListener {
            val etSkill = binding.etAddSkill.text.toString()
            val enId = prefHelper.getString(Constant.EN_ID)
            if (etSkill.isEmpty()) {
                Toast.makeText(requireContext(), "All field must be filled", Toast.LENGTH_SHORT).show()
            } else {
                presenter?.createSkill(enId, etSkill)
                presenter?.getSkillByEngId(enId)
            }

        }

        val enId = prefHelper.getString(Constant.EN_ID)

        presenter?.bindToView(this)
        presenter?.getEngineerByEngId(enId)
        presenter?.getSkillByEngId(enId)

        return binding.root
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

        Glide.with(requireContext())
            .load(img)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(binding.profilePhoto)
    }


    override fun onResultSuccessGetEngineer(data: DetailEngineerResponse.Data) {
        setProfile(data.accountName, data.engineerJobTitle, data.engineerDomisili, data.engineerDeskripsi, data.accountEmail,
        data.engineerInstagram, data.engineerGithub, data.engineerGitlab, data.engineerPhoto)
    }

    override fun onResultSuccessGetSkill(list: List<SkillModel>) {
        (binding.rvListSkill.adapter as SkillAdapter).addList(list)
        binding.rvListSkill.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultSuccessCreateSkill() {
        Toast.makeText(requireContext(), "Success add skill", Toast.LENGTH_SHORT).show()
    }

    override fun onResultFail(message: String) {
        binding.rvListSkill.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.container1.visibility = View.GONE
        binding.container2.visibility = View.GONE
        binding.cvContainer6.visibility = View.GONE
        binding.engineerView3.visibility = View.GONE
        binding.engineerView4.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.container1.visibility = View.VISIBLE
        binding.container2.visibility = View.VISIBLE
        binding.cvContainer6.visibility = View.VISIBLE
        binding.engineerView3.visibility = View.VISIBLE
        binding.engineerView4.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onRecyclerViewItemClicked(position: Int) {
        prefHelper.put(Constant.SK_ID_CLICK, listSkill[position].skillId)
        val intent = Intent (activity, UpdateSkillActivity::class.java)
        activity!!.startActivity(intent)

        Toast.makeText(activity, prefHelper.getInteger(Constant.SK_ID_CLICK).toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        val enId = prefHelper.getString(Constant.EN_ID)

        presenter?.bindToView(this)
        presenter?.getEngineerByEngId(enId)
        presenter?.getSkillByEngId(enId)
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