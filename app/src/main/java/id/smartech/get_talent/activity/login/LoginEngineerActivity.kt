package id.smartech.get_talent.activity.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.OnBoardActivity
import id.smartech.get_talent.activity.register.RegisterEngineerActivity
import id.smartech.get_talent.activity.ResetPasswordActivity
import id.smartech.get_talent.databinding.ActivityLoginEngineerBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.*

class LoginEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginEngineerBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_engineer)
        prefHelper = PrefHelper(this)
        val service = ApiClient.getApiClient(this)?.create(AccountService::class.java)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.setSharedPreferences(prefHelper)

        if (service != null) {
            viewModel.setLoginService(service)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterEngineerActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.requestFocus()
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Password tidak boleh kosong"
                binding.etPassword.requestFocus()
            } else {
                viewModel.loginRequest(email, password)
            }
        }
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isLoginLiveData.observe(this) {
            Log.d("subscribeLiveData", "$it")

            if (it) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, EngineerMainActivity::class.java))

                finish()
            } else {
                Toast.makeText(this, "Username / password is wrong!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }

//    override fun onStart() {
//        super.onStart()
//        if (prefHelper.getBoolean(Constant.IS_LOGIN)) {
//            moveIntent()
//        }
//    }

}