package id.smartech.get_talent.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
import kotlinx.coroutines.*

class LoginEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginEngineerBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: AccountService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_engineer)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(AccountService::class.java)


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
                loginRequest(email, password)
            }
        }

    }

//    override fun onStart() {
//        super.onStart()
//        if (prefHelper.getBoolean(Constant.IS_LOGIN)) {
//            moveIntent()
//        }
//    }

    private fun saveSession(accountId: String, token: String, level: String){
        prefHelper.put( Constant.IS_LOGIN, true )
        prefHelper.put(Constant.ACC_ID, accountId)
        prefHelper.put(Constant.TOKEN, token)
        prefHelper.put(Constant.ACC_LEVEL, level)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }

    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
        finish()
    }

    private fun loginRequest(email: String, password: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is LoginResponse) {
                if(result.success) {
                    saveSession(result.data.accountId, result.data.token, result.data.accountLevel )
                    moveIntent()
                }
            } else {
                Toast.makeText(this@LoginEngineerActivity, "Email / password is not registered!", Toast.LENGTH_LONG).show()
            }

        }
    }
}