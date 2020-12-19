package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityLoginCompanyBinding

class LoginCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginCompanyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_company)

        binding.btnLogin.setOnClickListener { startActivity(Intent(this, CompanyMainActivity::class.java)) }
        binding.tvRegister.setOnClickListener { startActivity(Intent(this, RegisterCompanyActivity::class.java)) }
        binding.tvForgotPassword.setOnClickListener { startActivity(Intent(this, ResetPasswordActivity::class.java))}
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }
}