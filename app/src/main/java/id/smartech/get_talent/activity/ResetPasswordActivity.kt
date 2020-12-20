package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityResetPasswordBinding
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        prefHelper = PrefHelper(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()

            val checkEmail = prefHelper.getString(Constant.ACC_EMAIL)

            if (email != checkEmail) {
                Toast.makeText(this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, ConfirmPasswordActivity::class.java))
            }

        }
    }
}