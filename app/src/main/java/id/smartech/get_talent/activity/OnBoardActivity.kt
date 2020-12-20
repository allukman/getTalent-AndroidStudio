package id.smartech.get_talent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ActivityOnBoardBinding
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper

class OnBoardActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnBoardBinding
    lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)
        prefHelper = PrefHelper(this)

        binding.btnTalent.setOnClickListener { startActivity(Intent(this, LoginEngineerActivity::class.java))}
        binding.btnCompany.setOnClickListener { startActivity(Intent(this, LoginCompanyActivity::class.java)) }
    }

    override fun onStart() {
        super.onStart()
        if (prefHelper.getBoolean(Constant.IS_LOGIN)) {
            val level = prefHelper.getInteger(Constant.ACC_LEVEL)

            if (level == 1) {
                startActivity(Intent(this, EngineerMainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, CompanyMainActivity::class.java))
                finish()
            }
        }
    }

    private fun moveIntent(){
        startActivity(Intent(this, EngineerMainActivity::class.java))
        finish()
    }
}