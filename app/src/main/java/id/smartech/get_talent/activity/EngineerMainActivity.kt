package id.smartech.get_talent.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityMainBinding
import id.smartech.get_talent.fragment.EngineerProfileFragment
import id.smartech.get_talent.fragment.FilterDialogFragment
import id.smartech.get_talent.fragment.HomeFragment
import id.smartech.get_talent.fragment.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class EngineerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val homeFragment = HomeFragment()
        val engineerProfileFragment = EngineerProfileFragment()
        val searchFragment = SearchFragment()

        currentFragment(engineerProfileFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> currentFragment(homeFragment)
                R.id.ic_search -> currentFragment(searchFragment)
                R.id.ic_profile -> currentFragment(engineerProfileFragment)
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