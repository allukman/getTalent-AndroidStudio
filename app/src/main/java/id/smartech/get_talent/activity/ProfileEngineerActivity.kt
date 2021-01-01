package id.smartech.get_talent.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityProfileEngineerBinding
import id.smartech.get_talent.helper.TabPagerAdaptor
import id.smartech.get_talent.util.PrefHelper

class ProfileEngineerActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: TabPagerAdaptor
    private lateinit var binding: ActivityProfileEngineerBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_engineer)
        prefHelper = PrefHelper(this)



        pagerAdapter = TabPagerAdaptor(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}