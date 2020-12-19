package id.smartech.get_talent.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityCompanyMainBinding
import id.smartech.get_talent.fragment.CompanyProfileFragment
import id.smartech.get_talent.fragment.EngineerProfileFragment
import id.smartech.get_talent.fragment.HomeFragment
import id.smartech.get_talent.fragment.SearchFragment

class CompanyMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_main)

        val homeFragment = HomeFragment()
        val companyProfileFragment = CompanyProfileFragment()
        val searchFragment = SearchFragment()

        currentFragment(companyProfileFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> currentFragment(homeFragment)
                R.id.ic_search -> currentFragment(searchFragment)
                R.id.ic_profile -> currentFragment(companyProfileFragment)
            }
            true
        }
    }


    private fun currentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}