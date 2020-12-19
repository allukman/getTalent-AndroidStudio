package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityOnBoardBinding

class OnBoardActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)

        binding.btnTalent.setOnClickListener { startActivity(Intent(this, LoginEngineerActivity::class.java))}
        binding.btnCompany.setOnClickListener { startActivity(Intent(this, LoginCompanyActivity::class.java)) }
    }
}