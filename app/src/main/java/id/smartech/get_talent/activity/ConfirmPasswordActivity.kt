package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityConfirmPasswordBinding
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class ConfirmPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPasswordBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_password)
        prefHelper = PrefHelper(this)

        binding.btnReset.setOnClickListener {
            val password = binding.etAccPassword.text.toString()
            val confirmPassword = binding.etAccConfirmPassword.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password harus sama dengan konfirmasi password", Toast.LENGTH_SHORT).show()
            } else {
                saveSession(password)
                startActivity(Intent(this, LoginResetActivity::class.java))
            }
        }

    }

    private fun saveSession(password: String){
        prefHelper.put( Constant.ACC_PASSWORD, password )
    }
}