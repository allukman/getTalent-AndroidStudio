package id.smartech.get_talent.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityProfileEngineerBinding
import id.smartech.get_talent.helper.TabPagerAdaptor

class ProfileEngineerActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: TabPagerAdaptor
    private lateinit var binding: ActivityProfileEngineerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_engineer)

        pagerAdapter = TabPagerAdaptor(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}