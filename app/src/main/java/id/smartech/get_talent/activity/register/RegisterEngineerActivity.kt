package id.smartech.get_talent.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.OnBoardActivity
import id.smartech.get_talent.activity.login.LoginEngineerActivity
import id.smartech.get_talent.databinding.ActivityRegisterEngineerBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.AccountService
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEngineerBinding
    private lateinit var prefHelper: PrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: AccountService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_engineer)
        prefHelper = PrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(AccountService::class.java)


        binding.tvLogin.setOnClickListener { startActivity(Intent(this, LoginEngineerActivity::class.java)) }
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
                registerEngineer(name, email, nohp, password)
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

    private fun registerEngineer(name: String, email: String, phone: String, password: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerEngineer(name, email, phone, password, 1)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is RegisterResponse) {
                if(result.success) {
                    Toast.makeText(this@RegisterEngineerActivity, "Register Success!", Toast.LENGTH_SHORT).show()
                    moveIntent()
                }
            } else {
                Toast.makeText(this@RegisterEngineerActivity, "email has been registered!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}