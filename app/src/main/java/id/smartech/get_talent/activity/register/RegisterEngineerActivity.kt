package id.smartech.get_talent.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.OnBoardActivity
import id.smartech.get_talent.activity.login.LoginEngineerActivity
import id.smartech.get_talent.activity.login.LoginViewModel
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.databinding.ActivityRegisterEngineerBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.PrefHelper
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.*

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEngineerBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_engineer)
        prefHelper = PrefHelper(this)
        val service = ApiClient.getApiClient(this)?.create(AccountService::class.java)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        if (service != null) {
            viewModel.setRegisterService(service)
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginEngineerActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etAccNama.text.toString()
            val email = binding.etAccEmail.text.toString()
            val nohp = binding.etAccPhone.text.toString()
            val password = binding.etAccPassword.text.toString()
            val confirmPassword = binding.etAccConfirmPassword.text.toString()

            if(name.isEmpty() || email.isEmpty() || nohp.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data terlebih dahulu", Toast.LENGTH_LONG).show()
                binding.etAccNama.requestFocus()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi password harus sama", Toast.LENGTH_LONG).show()
                binding.etAccNama.requestFocus()
            } else {
                viewModel.registerEngineer(name, email, nohp, password)
            }
        }

        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isRegisterLiveData.observe(this) {
            Log.d("subscribeLiveData", "$it")

            if (it) {
                Toast.makeText(this, "Register Success", Toast.LENGTH_LONG).show()
                moveIntent()
            } else {
                Toast.makeText(this, "Register Failed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }

    private fun moveIntent(){
        startActivity(Intent(this, LoginEngineerActivity::class.java))
        finish()
    }

//    private fun registerEngineer(name: String, email: String, phone: String, password: String) {
//        coroutineScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                try {
//                    service.registerEngineer(name, email, phone, password, 1)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//
//            if(result is RegisterResponse) {
//                if(result.success) {
//                    Toast.makeText(this@RegisterEngineerActivity, "Register Success!", Toast.LENGTH_SHORT).show()
//                    moveIntent()
//                }
//            } else {
//                Toast.makeText(this@RegisterEngineerActivity, "email has been registered!", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }
}