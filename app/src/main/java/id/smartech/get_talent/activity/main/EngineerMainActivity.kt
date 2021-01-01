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
import id.smartech.get_talent.databinding.ActivityMainBinding
import id.smartech.get_talent.fragment.*
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class EngineerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: AccountService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(AccountService::class.java)

        setSupportActionBar(binding.topToolbar)

        val homeFragment = HomeFragment()
        val engineerProfileFragment = EngineerProfileFragment()
        val searchFragment = SearchFragment()
        val projectFragment = ProjectFragment()

        currentFragment(homeFragment)

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
                    currentFragment(engineerProfileFragment)
                    binding.toolbarTitle.setText("Profile  ")

                }
                R.id.ic_project -> {
                    currentFragment(projectFragment)
                    binding.toolbarTitle.setText("Project  ")
                }
            }
            true
        }
        getEngineerId()
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

    private fun showDialog() {
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

    private fun saveSession(engineerId: String, accountEmail: String){
        prefHelper.put(Constant.EN_ID, engineerId )
        prefHelper.put(Constant.ACC_EMAIL, accountEmail)
    }

    private fun getEngineerId() {
        coroutineScope.launch {
            Log.d("android", "Start: ${Thread.currentThread().name}")
            val response = withContext(Dispatchers.IO) {

                Log.d("android", "CallAPI : ${Thread.currentThread().name}")
                try {
                    service?.getEngineerIdByAccountId(prefHelper.getString(Constant.ACC_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("android response", response.toString())

            if (response is GetEngineerIdResponse) {
                if (response.success) {
                    saveSession(response.data.engineerId, response.data.accountEmail)
                    Toast.makeText(this@EngineerMainActivity, prefHelper.getString(Constant.EN_ID), Toast.LENGTH_SHORT).show()
                } else {

                }
            }
        }
    }

}