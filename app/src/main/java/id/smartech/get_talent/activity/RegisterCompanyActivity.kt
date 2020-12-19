package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityRegisterCompanyBinding

class RegisterCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterCompanyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_company)

        binding.tvLogin.setOnClickListener { startActivity(Intent(this, LoginCompanyActivity::class.java)) }
        binding.btnRegister.setOnClickListener {
            Toast.makeText(this, "Berhasil daftar", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginCompanyActivity::class.java))
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }
}