package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityLoginResetBinding
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class LoginResetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginResetBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_reset)
        prefHelper = PrefHelper(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etAccPassword.text.toString()

            val checkEmail = prefHelper.getString( Constant.ACC_EMAIL)
            val checkPassword = prefHelper.getString(Constant.ACC_PASSWORD)
            val checkLevel = prefHelper.getInteger(Constant.ACC_LEVEL)

            if (email.isEmpty()) {
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.requestFocus()
            } else if (password.isEmpty()) {
                binding.etAccPassword.error = "Password tidak boleh kosong"
                binding.etAccPassword.requestFocus()
            } else if (email != checkEmail || password != checkPassword){
                Toast.makeText(this, "Username / password salah!", Toast.LENGTH_SHORT).show()
            } else {
                if (checkLevel == 1 ) {
                    startActivity(Intent(this, ProfileEngineerActivity::class.java))
                    saveSession()
                } else {
                    startActivity(Intent(this, ProfileCompanyActivity::class.java))
                    saveSession()
                }
            }
        }

    }


    private fun saveSession(){
        prefHelper.put( Constant.IS_LOGIN, true )
    }
}