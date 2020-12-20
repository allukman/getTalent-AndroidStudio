package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityLoginCompanyBinding
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class LoginCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginCompanyBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_company)
        prefHelper = PrefHelper(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val checkEmail = prefHelper.getString( Constant.ACC_EMAIL)
            val checkPassword = prefHelper.getString(Constant.ACC_PASSWORD)
            val checkLevel = prefHelper.getInteger(Constant.ACC_LEVEL)

            if (email.isEmpty()) {
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.requestFocus()
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Password tidak boleh kosong"
                binding.etPassword.requestFocus()
            } else if (checkEmail != email || checkPassword != password || checkLevel != 2) {
                Toast.makeText(this, "Username / password salah!", Toast.LENGTH_SHORT).show()
            }
            else {
                moveIntent()
                saveSession()
            }

        }
        binding.tvRegister.setOnClickListener { startActivity(Intent(this, RegisterCompanyActivity::class.java)) }
        binding.tvForgotPassword.setOnClickListener { startActivity(Intent(this, ResetPasswordActivity::class.java))}
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }

    private fun moveIntent(){
        startActivity(Intent(this, CompanyMainActivity::class.java))
        finish()
    }

    private fun saveSession(){
        prefHelper.put( Constant.IS_LOGIN, true )
    }
}