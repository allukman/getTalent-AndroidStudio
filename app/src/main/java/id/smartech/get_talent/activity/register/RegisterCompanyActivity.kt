package id.smartech.get_talent.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.OnBoardActivity
import id.smartech.get_talent.activity.login.LoginCompanyActivity
import id.smartech.get_talent.activity.login.LoginEngineerActivity
import id.smartech.get_talent.databinding.ActivityRegisterCompanyBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class RegisterCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterCompanyBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: AccountService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_company)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(AccountService::class.java)

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginCompanyActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etAccNama.text.toString()
            val email = binding.etAccEmail.text.toString()
            val nohp = binding.etAccPhone.text.toString()
            val password = binding.etAccPassword.text.toString()
            val confirmPassword = binding.etAccConfirmPassword.text.toString()
            val comName = binding.etComName.text.toString()
            val comPosition = binding.etComPosition.text.toString()

            if(name.isEmpty() || email.isEmpty() || nohp.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || comName.isEmpty() || comPosition.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data terlebih dahulu", Toast.LENGTH_LONG).show()
                binding.etAccNama.requestFocus()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi password harus sama", Toast.LENGTH_LONG).show()
                binding.etAccNama.requestFocus()
            } else {
                registerCompany(name, email, nohp, password, comName, comPosition)
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }

    private fun registerCompany(name: String, email: String, phone: String, password: String, companyName: String, companyPosition: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerCompany(name, email, phone, password, 2, companyName , companyPosition)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is RegisterResponse) {
                if(result.success) {
                    Toast.makeText(this@RegisterCompanyActivity, "Register Success!", Toast.LENGTH_SHORT).show()
                    moveIntent()
                }
            } else {
                Toast.makeText(this@RegisterCompanyActivity, "email has been registered!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveIntent(){
        startActivity(Intent(this, LoginCompanyActivity::class.java))
        finish()
    }
}