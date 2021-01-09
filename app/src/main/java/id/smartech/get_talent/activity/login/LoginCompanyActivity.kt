package id.smartech.get_talent.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.CompanyMainActivity
import id.smartech.get_talent.activity.OnBoardActivity
import id.smartech.get_talent.activity.register.RegisterCompanyActivity
import id.smartech.get_talent.activity.ResetPasswordActivity
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.databinding.ActivityLoginCompanyBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class LoginCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginCompanyBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_company)
        prefHelper = PrefHelper(this)
        val service = ApiClient.getApiClient(this)?.create(AccountService::class.java)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.setSharedPreferences(prefHelper)

        if (service != null) {
            viewModel.setLoginService(service)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterCompanyActivity::class.java)) }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))}

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
                startActivity(Intent(this, CompanyMainActivity::class.java))

                finish()
            } else {
                Toast.makeText(this, "Username / password is wrong!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }
}