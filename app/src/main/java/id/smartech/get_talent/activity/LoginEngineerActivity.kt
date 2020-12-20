package id.smartech.get_talent.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityLoginEngineerBinding

class LoginEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginEngineerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_engineer)

        binding.tvRegister.setOnClickListener { startActivity(Intent(this, RegisterEngineerActivity::class.java)) }
        binding.tvForgotPassword.setOnClickListener { startActivity(Intent(this, ResetPasswordActivity::class.java))}

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
                startActivity(Intent(this, EngineerMainActivity::class.java))
            }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }
}