package id.smartech.get_talent.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityCompanyMainBinding
import id.smartech.get_talent.fragment.CompanyProfileFragment
import id.smartech.get_talent.fragment.EngineerProfileFragment
import id.smartech.get_talent.fragment.HomeFragment
import id.smartech.get_talent.fragment.SearchFragment
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class CompanyMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyMainBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_main)
        prefHelper = PrefHelper(this)


        setSupportActionBar(binding.topToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.topToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                showDialog()
            }
        }
        return true
    }

    fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure!")
        builder.setMessage("Do you want to logout?")
        builder.setPositiveButton("Yes",{ dialogInterface: DialogInterface, i: Int ->
            prefHelper.put( Constant.IS_LOGIN, false )
            moveIntent()
        })
        builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    private fun moveIntent(){
        startActivity(Intent(this, OnBoardActivity::class.java))
        finish()
    }
}