package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityRegisterCompanyBinding
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class RegisterCompanyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterCompanyBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_company)
        prefHelper = PrefHelper(this)

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginCompanyActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etAccNama.text.toString()
            val email = binding.etAccEmail.text.toString()
            val comName = binding.etComName.text.toString()
            val comPosition = binding.etComPosition.text.toString()
            val nohp = binding.etAccPhone.text.toString()
            val password = binding.etAccPassword.text.toString()
            val confirmPassword = binding.etAccConfirmPassword.text.toString()

            if(name.isEmpty() || email.isEmpty() || nohp.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || comName.isEmpty() || comPosition.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data terlebih dahulu", Toast.LENGTH_LONG).show()
                binding.etAccNama.requestFocus()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi password harus sama", Toast.LENGTH_LONG).show()
                binding.etAccNama.requestFocus()
            } else {
                Toast.makeText(this, "Berhasil daftar", Toast.LENGTH_LONG).show()
                saveSession(comName, email, password)
                startActivity(Intent(this, LoginCompanyActivity::class.java))
            }
        }
    }

    private fun saveSession(comName: String, email: String, password: String){
        prefHelper.put( Constant.COM_NAME, comName )
        prefHelper.put(Constant.ACC_EMAIL, email)
        prefHelper.put( Constant.ACC_PASSWORD, password )
        prefHelper.put( Constant.ACC_LEVEL, 2)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }
}