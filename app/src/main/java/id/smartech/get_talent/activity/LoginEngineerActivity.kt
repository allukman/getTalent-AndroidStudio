package id.smartech.get_talent.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityLoginEngineerBinding
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class LoginEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginEngineerBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_engineer)
        prefHelper = PrefHelper(this)


        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterEngineerActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }


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
            } else if (email != checkEmail || password != checkPassword || checkLevel != 1) {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_LONG).show()
            } else {
                moveIntent()
                saveSession()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (prefHelper.getBoolean(Constant.IS_LOGIN)) {
            moveIntent()
        }
    }

    private fun saveSession(){
        prefHelper.put( Constant.IS_LOGIN, true )
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }

    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
        finish()
    }
}