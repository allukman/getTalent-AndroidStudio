package id.smartech.get_talent.activity.detailProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.hire.AddHireActivity
import id.smartech.get_talent.databinding.ActivityProfileEngineerBinding
import id.smartech.get_talent.helper.TabPagerAdaptor
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.EngineerApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.android.synthetic.main.activity_profile_engineer.*
import kotlinx.coroutines.*

class ProfileEngineerActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: TabPagerAdaptor
    private lateinit var binding: ActivityProfileEngineerBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_engineer)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(EngineerApiService::class.java)

        pagerAdapter = TabPagerAdaptor(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        getEngineerByEngId()

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

    private fun getEngineerByEngId(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerByEngId(prefHelper.getString(Constant.EN_ID_CLICK))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }

            }

            if (response is DetailEngineerResponse) {
                if (response.success) {
                    Toast.makeText(this@ProfileEngineerActivity, response.data.accountName, Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}