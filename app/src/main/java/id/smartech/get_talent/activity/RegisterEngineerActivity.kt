package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityRegisterEngineerBinding
import id.smartech.get_talent.helper.TabPagerAdaptor
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEngineerBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_engineer)
        prefHelper = PrefHelper(this)
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
                Toast.makeText(this, "Berhasil daftar", Toast.LENGTH_LONG).show()
                saveSession(name, email, password)
                startActivity(Intent(this, LoginEngineerActivity::class.java))
            }

        }
    }

    private fun saveSession(name: String, email: String, password: String){
        prefHelper.put( Constant.ACC_NAMA, name )
        prefHelper.put(Constant.ACC_EMAIL, email)
        prefHelper.put( Constant.ACC_PASSWORD, password )
        prefHelper.put( Constant.ACC_LEVEL, 1)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }
}