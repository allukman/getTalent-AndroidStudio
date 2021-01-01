package id.smartech.get_talent.activity.main

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.OnBoardActivity
import id.smartech.get_talent.activity.home.HomeFragment
import id.smartech.get_talent.databinding.ActivityCompanyMainBinding
import id.smartech.get_talent.fragment.*
import id.smartech.get_talent.activity.project.ListProjectCompanyFragment
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class CompanyMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyMainBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: AccountService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_main)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(AccountService::class.java)


        setSupportActionBar(binding.topToolbar)

        val homeFragment = HomeFragment()
        val companyProfileFragment = CompanyProfileFragment()
        val searchFragment = SearchFragment()
        val projectFragment = ListProjectCompanyFragment()

        currentFragment(companyProfileFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {
                    currentFragment(homeFragment)
                    binding.toolbarTitle.setText("Home   ")
                }
                R.id.ic_search -> {
                    currentFragment(searchFragment)
                    binding.toolbarTitle.setText("Search  ")
                }
                R.id.ic_profile -> {
                    currentFragment(companyProfileFragment)
                    binding.toolbarTitle.setText("Profile  ")

                }
                R.id.ic_project -> {
                    currentFragment(projectFragment)
                    binding.toolbarTitle.setText(prefHelper.getString(Constant.COM_ID))
                }
            }
            true
        }

        getCompanyId()
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
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            prefHelper.put( Constant.IS_LOGIN, false )
            moveIntent()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    private fun moveIntent(){
        startActivity(Intent(this, OnBoardActivity::class.java))
        finish()
    }

    private fun saveSession(companyId: String?, accountEmail: String?){
        prefHelper.put(Constant.COM_ID, companyId )
        prefHelper.put(Constant.ACC_EMAIL, accountEmail)
    }

    private fun getCompanyId() {
        coroutineScope.launch {
            Log.d("android", "Start: ${Thread.currentThread().name}")
            val response = withContext(Dispatchers.IO) {

                Log.d("android", "CallAPI : ${Thread.currentThread().name}")
                try {
                    service?.getCompanyIdByAccountId(prefHelper.getString(Constant.ACC_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("android response", response.toString())

            if (response is GetCompanyIdResponse) {
                if (response.success) {
                    saveSession(response.data.comId, response.data.accountEmail)
                } else {

                }
            }
        }
    }
}