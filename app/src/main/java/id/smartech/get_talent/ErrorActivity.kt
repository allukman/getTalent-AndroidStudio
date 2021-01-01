package id.smartech.get_talent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.activity.OnBoardActivity
import id.smartech.get_talent.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_error)

        binding.btnTryGain.setOnClickListener {
            startActivity(Intent(this, OnBoardActivity::class.java))
        }
    }
}