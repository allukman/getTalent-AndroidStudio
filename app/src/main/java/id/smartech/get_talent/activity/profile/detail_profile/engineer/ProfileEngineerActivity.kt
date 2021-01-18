package id.smartech.get_talent.activity.profile.detail_profile.engineer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.hire.AddHireActivity
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.activity.skill.GetSkillResponse
import id.smartech.get_talent.activity.skill.SkillAdapter
import id.smartech.get_talent.activity.skill.SkillModel
import id.smartech.get_talent.databinding.ActivityProfileEngineerBinding
import id.smartech.get_talent.helper.TabPagerAdaptor
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.service.SkillApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*


class ProfileEngineerActivity : AppCompatActivity(), OnRecyclerViewClickListener, DetailEngineerContract.View {

    private lateinit var pagerAdapter: TabPagerAdaptor
    private lateinit var binding: ActivityProfileEngineerBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var serviceEngineer: EngineerApiService
    private lateinit var serviceSkill: SkillApiService

    private var presenter: DetailEngineerContract.Presenter? = null
    private var listSkill = ArrayList<SkillModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_engineer)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        serviceEngineer = ApiClient.getApiClient(this)!!.create(EngineerApiService::class.java)
        serviceSkill = ApiClient.getApiClient(this)!!.create(SkillApiService::class.java)
        presenter = DetailEngineerPresenter(coroutineScope, serviceEngineer, serviceSkill)

        pagerAdapter = TabPagerAdaptor(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.rvListSkill.adapter = SkillAdapter(listSkill, this)

        val recyclerView = binding.rvListSkill
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        recyclerView.layoutManager = layoutManager

        val enIdClick = prefHelper.getString(Constant.EN_ID_CLICK)

        presenter?.getEngineerByEngId(enIdClick)
        presenter?.getSkillByEngId(enIdClick)

        var level = prefHelper.getString(Constant.ACC_LEVEL)

        if (level == "1") {
            binding.btnHire.visibility = View.GONE
        } else {
            binding.btnHire.visibility = View.VISIBLE
        }

        binding.btnHire.setOnClickListener {
            startActivity(Intent(this, AddHireActivity::class.java))
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

    override fun onRecyclerViewItemClicked(position: Int) {
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

    override fun onResultFail(message: String) {
        binding.rvListSkill.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.engineerView.visibility = View.GONE
        binding.engineerView2.visibility = View.GONE
        binding.engineerView3.visibility = View.GONE
        binding.engineerView4.visibility = View.GONE

    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.engineerView.visibility = View.VISIBLE
        binding.engineerView2.visibility = View.VISIBLE
        binding.engineerView3.visibility = View.VISIBLE
        binding.engineerView4.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unBind()
    }


}