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

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEngineerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_engineer)

        binding.tvLogin.setOnClickListener { startActivity(Intent(this, LoginEngineerActivity::class.java)) }

        binding.btnRegister.setOnClickListener {
            Toast.makeText(this, "Berhasil daftar", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginEngineerActivity::class.java))
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, OnBoardActivity::class.java))
    }
}